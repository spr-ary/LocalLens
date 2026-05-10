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

    when (screen) {
        "home" -> HomeScreen(
            stalls = UserFakeData.stalls,
            favorites = favorites,
            onStallClick = { openDetail(it) },
            onMapClick = { screen = "map" },
            onFavoriteClick = { screen = "favorite" }
        )

        "detail" -> selectedStall?.let { stall ->
            DetailScreen(
                stall = stall,
                isFavorite = favorites.contains(stall.id),
                onBack = { screen = "home" },
                onFavoriteToggle = {
                    if (favorites.contains(stall.id)) favorites.remove(stall.id)
                    else favorites.add(stall.id)
                }
            )
        }

        "favorite" -> FavoriteScreen(
            stalls = UserFakeData.stalls.filter { favorites.contains(it.id) },
            onHomeClick = { screen = "home" },
            onMapClick = { screen = "map" },
            onStallClick = { openDetail(it) }
        )

        "map" -> MapScreen(
            stalls = UserFakeData.stalls,
            onHomeClick = { screen = "home" },
            onFavoriteClick = { screen = "favorite" },
            onStallClick = { openDetail(it) }
        )

        else -> HomeScreen(
            stalls = UserFakeData.stalls,
            favorites = favorites,
            onStallClick = { openDetail(it) },
            onMapClick = { screen = "home" },
            onFavoriteClick = { screen = "home" }
        )
    }
}