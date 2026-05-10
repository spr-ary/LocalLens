package org.classapp.locallens.ui.user

import androidx.compose.runtime.*
import org.classapp.locallens.data.UserFakeData
import org.classapp.locallens.model.UserStall

@Composable
fun UserApp() {
    var screen by remember { mutableStateOf("home") }
    var selectedStall by remember { mutableStateOf<UserStall?>(null) }
    val favorites = remember { mutableStateListOf<String>() }

    fun openDetail(stall: UserStall) {
        selectedStall = stall
        screen = "detail"
    }

    fun goHome() {
        selectedStall = null
        screen = "home"
    }

    when (screen) {
        "home" -> HomeScreen(
            stalls = UserFakeData.stalls,
            favorites = favorites,
            onStallClick = { openDetail(it) },
            onMapClick = { screen = "map" },
            onFavoriteClick = { screen = "favorite" },
            onProfileClick = { screen = "profile" }
        )

        "detail" -> selectedStall?.let { stall ->
            DetailScreen(
                stall = stall,
                isFavorite = favorites.contains(stall.id),
                onBack = { goHome() },
                onFavoriteToggle = {
                    if (favorites.contains(stall.id)) favorites.remove(stall.id)
                    else favorites.add(stall.id)
                }
            )
        }

        "map" -> MapScreen(
            stalls = UserFakeData.stalls,
            onHomeClick = { goHome() },
            onFavoriteClick = { screen = "favorite" },
            onProfileClick = { screen = "profile" },
            onStallClick = { openDetail(it) }
        )

        "favorite" -> FavoriteScreen(
            stalls = UserFakeData.stalls.filter { favorites.contains(it.id) },
            onHomeClick = { goHome() },
            onMapClick = { screen = "map" },
            onProfileClick = { screen = "profile" },
            onStallClick = { openDetail(it) }
        )

        "profile" -> ProfileScreen(
            savedCount = favorites.size,
            onHomeClick = { goHome() },
            onMapClick = { screen = "map" },
            onFavoriteClick = { screen = "favorite" }
        )
    }
}