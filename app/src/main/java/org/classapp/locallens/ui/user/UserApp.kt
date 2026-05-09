package org.classapp.locallens.ui.user

import androidx.compose.runtime.Composable
import org.classapp.locallens.data.UserFakeData

@Composable
fun UserApp() {

    HomeScreen(
        stalls = UserFakeData.stalls,
        favorites = emptyList(),
        onStallClick = {},
        onMapClick = {},
        onFavoriteClick = {}
    )
}