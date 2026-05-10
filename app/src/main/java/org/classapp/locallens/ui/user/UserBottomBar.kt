package org.classapp.locallens.ui.user

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

enum class UserTab {
    HOME, MAP, SAVE, PROFILE
}

@Composable
fun UserBottomBar(
    selectedTab: UserTab,
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onSaveClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == UserTab.HOME,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedTab == UserTab.MAP,
            onClick = onMapClick,
            icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            label = { Text("Explore") }
        )
        NavigationBarItem(
            selected = selectedTab == UserTab.SAVE,
            onClick = onSaveClick,
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text("Save") }
        )
        NavigationBarItem(
            selected = selectedTab == UserTab.PROFILE,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}