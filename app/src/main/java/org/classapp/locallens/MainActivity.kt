package org.classapp.locallens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.classapp.locallens.ui.LocalLensApp
import org.classapp.locallens.ui.theme.LocalLensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalLensTheme {
                LocalLensApp()
            }
        }
    }
}
