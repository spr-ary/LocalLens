package org.classapp.locallens.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.AppUser
import org.classapp.locallens.model.UserRole

private enum class AuthMode {
    Login,
    Register
}

@Composable
fun AuthScreen(
    errorMessage: String? = null,
    onLogin: (username: String, password: String) -> Unit,
    onRegister: (user: AppUser, password: String) -> Unit
) {
    var mode by rememberSaveable { mutableStateOf(AuthMode.Login) }
    var username by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var role by rememberSaveable { mutableStateOf(UserRole.StallOwner) }
    var formErrorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE9E4FF), MaterialTheme.colorScheme.background)
                )
            )
            .padding(22.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "LocalLens",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (mode == AuthMode.Login) "Login to continue" else "Create your account",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = mode == AuthMode.Login,
                        onClick = { mode = AuthMode.Login; formErrorMessage = null },
                        label = { Text("Login") }
                    )
                    FilterChip(
                        selected = mode == AuthMode.Register,
                        onClick = { mode = AuthMode.Register; formErrorMessage = null },
                        label = { Text("Register") }
                    )
                }

                if (mode == AuthMode.Register) {
                    AuthTextField("Name", name, { name = it })
                    AuthTextField("Phone", phone, { phone = it }, KeyboardType.Phone)
                    Text("Register as", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        UserRole.entries.forEach { option ->
                            FilterChip(
                                selected = role == option,
                                onClick = { role = option },
                                label = { Text(option.label) }
                            )
                        }
                    }
                }

                AuthTextField("Username", username, { username = it })
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                (formErrorMessage ?: errorMessage)?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                }

                Button(
                    onClick = {
                        formErrorMessage = if (mode == AuthMode.Login) {
                            if (username.isBlank()) {
                                "Please enter your username."
                            } else if (!username.isValidUsername()) {
                                "Username can use letters, numbers, dots, and underscores only."
                            } else if (password.isBlank()) {
                                "Please enter your password."
                            } else {
                                onLogin(username.trim(), password)
                                null
                            }
                        } else {
                            when {
                                username.isBlank() -> "Please enter your username."
                                !username.isValidUsername() -> "Username can use letters, numbers, dots, and underscores only."
                                name.isBlank() -> "Please enter your name."
                                password.length < 6 -> "Password must be at least 6 characters."
                                else -> {
                                    onRegister(
                                        AppUser(
                                            id = "",
                                            username = username.trim(),
                                            name = name.trim(),
                                            phone = phone.trim(),
                                            role = role
                                        ),
                                        password
                                    )
                                    null
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (mode == AuthMode.Login) "Login" else "Register")
                }

                Spacer(modifier = Modifier.height(4.dp))
                TextButton(
                    onClick = {
                        if (mode == AuthMode.Login) {
                            mode = AuthMode.Register
                            username = ""
                            password = ""
                        } else {
                            mode = AuthMode.Login
                            username = ""
                            password = ""
                        }
                        formErrorMessage = null
                    }
                ) {
                    Text(if (mode == AuthMode.Login) "Don't have an account? Register" else "Already have an account? Login")
                }
            }
        }
    }
}

private fun String.isValidUsername(): Boolean {
    return matches(Regex("^[A-Za-z0-9._]{3,30}$"))
}

@Composable
private fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}
