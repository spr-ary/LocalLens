package org.classapp.locallens.ui.user

import androidx.compose.runtime.*
import org.classapp.locallens.data.UserFakeData
import org.classapp.locallens.model.UserStall
import org.classapp.locallens.data.FirestoreRepository
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun UserApp(
    userName: String = "User",
    onLogout: () -> Unit
){  var screen by remember { mutableStateOf("home") }
    var selectedStall by remember { mutableStateOf<UserStall?>(null) }
    val favorites = remember { mutableStateListOf<String>() }

    var stalls by remember { mutableStateOf<List<UserStall>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        runCatching {
            FirestoreRepository.loadCustomerStalls()
        }.onSuccess {
            stalls = it
            isLoading = false
        }.onFailure {
            errorMessage = it.message
            isLoading = false
        }
    }

    fun openDetail(stall: UserStall) {
        selectedStall = stall
        screen = "detail"
    }

    fun goHome() {
        selectedStall = null
        screen = "home"
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    when (screen) {
        "home" -> HomeScreen(
            userName = userName,
            stalls = stalls,
            favorites = favorites,
            onStallClick = { openDetail(it) },
            onMapClick = { screen = "map" },
            onFavoriteClick = { screen = "favorite" },
            onProfileClick = { screen = "profile" },
            onSeeAllClick = {
                screen = "map"
            },
        )

        "detail" -> selectedStall?.let { stall ->
            DetailScreen(
                stall = stall,
                isFavorite = favorites.contains(stall.id),
                onBack = { goHome() },
                onFavoriteToggle = {
                    val id = stall.id.trim()

                    if (favorites.contains(id)) {
                        favorites.remove(id)
                    } else {
                        favorites.add(id)
                    }
                },
                onDirectionsClick = {
                    screen = "map"
                },
            )
        }

        "map" -> MapScreen(
            stalls = stalls,
            onHomeClick = { goHome() },
            onFavoriteClick = { screen = "favorite" },
            onProfileClick = { screen = "profile" },
            onStallClick = { openDetail(it) }
        )

        "favorite" -> FavoriteScreen(
            stalls = stalls.filter { stall ->
                favorites.contains(stall.id.trim())
            },
            onHomeClick = { screen = "home" },
            onMapClick = { screen = "map" },
            onProfileClick = { screen = "profile" },
            onStallClick = { openDetail(it) }
        )

        "profile" -> ProfileScreen(
            userName = userName,
            savedCount = favorites.size,
            onHomeClick = { screen = "home" },
            onMapClick = { screen = "map" },
            onFavoriteClick = { screen = "favorite" },
            onLogout = onLogout
        )
    }
}