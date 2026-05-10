package org.classapp.locallens.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import org.classapp.locallens.model.UserStall

@Composable
fun MapScreen(
    stalls: List<UserStall>,
    onHomeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onStallClick: (UserStall) -> Unit
) {
    val bangkok = LatLng(13.7563, 100.5018)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bangkok, 12f)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(false, onHomeClick, icon = { Text("🏠") }, label = { Text("Home") })
                NavigationBarItem(true, {}, icon = { Text("📍") }, label = { Text("Map") })
                NavigationBarItem(false, onFavoriteClick, icon = { Text("❤️") }, label = { Text("Save") })
                NavigationBarItem(false, {}, icon = { Text("👤") }, label = { Text("Profile") })
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                stalls.forEach { stall ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(stall.latitude, stall.longitude)
                        ),
                        title = stall.name,
                        snippet = stall.location,
                        onClick = {
                            onStallClick(stall)
                            true
                        }
                    )
                }
            }
        }
    }
}