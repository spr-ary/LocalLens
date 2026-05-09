package org.classapp.locallens.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoleSelectionScreen(
    onUserClick: () -> Unit,
    onOwnerClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Welcome to LocalLens")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onUserClick) {
            Text("Continue as User")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onOwnerClick) {
            Text("Continue as Owner")
        }
    }
}