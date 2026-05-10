package org.classapp.locallens.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import org.classapp.locallens.data.FirestoreRepository
import org.classapp.locallens.model.AppUser
import org.classapp.locallens.model.UserRole
import org.classapp.locallens.ui.auth.AuthScreen
import org.classapp.locallens.ui.customer.CustomerHomeScreen
import org.classapp.locallens.ui.owner.StoreOwnerApp

@Composable
fun LocalLensApp() {
    var currentUser by remember { mutableStateOf<AppUser?>(null) }
    var authError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    when (val user = currentUser) {
        null -> AuthScreen(
            errorMessage = authError,
            onLogin = { username, password ->
                scope.launch {
                    authError = null
                    runCatching {
                        FirestoreRepository.signInWithUsernamePassword(username, password)
                    }.onSuccess {
                        currentUser = it
                    }.onFailure {
                        authError = it.message
                    }
                }
            },
            onRegister = { draftUser, password ->
                scope.launch {
                    authError = null
                    runCatching {
                        FirestoreRepository.registerWithUsernamePassword(draftUser, password)
                    }.onSuccess {
                        currentUser = it
                    }.onFailure {
                        authError = it.message
                    }
                }
            }
        )

        else -> when (user.role) {
            UserRole.StallOwner -> StoreOwnerApp(
                owner = user,
                onLogout = { currentUser = null }
            )

            UserRole.Customer -> CustomerHomeScreen(
                user = user,
                onLogout = { currentUser = null }
            )
        }
    }
}
