package org.classapp.locallens.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onProfileClick: () -> Unit,
    onStallClick: (UserStall) -> Unit
) {
    val bangkok = LatLng(13.7563, 100.5018)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bangkok, 12f)
    }

    Scaffold(
        bottomBar = {
            UserBottomBar(
                selectedTab = UserTab.MAP,
                onHomeClick = onHomeClick,
                onMapClick = {},
                onSaveClick = onFavoriteClick,
                onProfileClick = onProfileClick
            )
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
                        state = MarkerState(LatLng(stall.latitude, stall.longitude)),
                        title = stall.name,
                        snippet = stall.location,
                        onClick = {
                            onStallClick(stall)
                            true
                        }
                    )
                }
            }

            stalls.firstOrNull()?.let { stall ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(18.dp)
                        .fillMaxWidth()
                        .clickable { onStallClick(stall) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .background(Color(0xFFE4DAFF), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stall.imageEmoji, style = MaterialTheme.typography.headlineMedium)
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(stall.name, style = MaterialTheme.typography.titleMedium)
                            Text(stall.category, color = Color.Gray)
                            Text("⭐ ${stall.rating}  •  ${stall.distance}")
                        }

                        Button(onClick = { onStallClick(stall) }) {
                            Text("View")
                        }
                    }
                }
            }
        }
    }
}