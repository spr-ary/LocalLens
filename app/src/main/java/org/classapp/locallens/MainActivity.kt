package org.classapp.locallens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.classapp.locallens.ui.owner.StoreOwnerApp
import org.classapp.locallens.ui.theme.LocalLensTheme
import androidx.compose.runtime.*
import org.classapp.locallens.ui.RoleSelectionScreen
import org.classapp.locallens.ui.user.UserApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalLensTheme {
                var screen by remember { mutableStateOf("role") }

                when (screen) {
                    "role" -> RoleSelectionScreen(
                        onUserClick = { screen = "user" },
                        onOwnerClick = { screen = "owner" }
                    )

                    "owner" -> StoreOwnerApp()

                    "user" -> UserApp()
                    }
                }
            }
        }
    }

