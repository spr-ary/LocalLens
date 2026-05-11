package org.classapp.locallens.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedStall by remember { mutableStateOf(stalls.firstOrNull()) }

    val categories = listOf("All", "Noodles", "Rice", "Dessert", "BBQ")

    val filteredStalls = stalls.filter { stall ->
        val matchesSearch =
            stall.name.contains(searchText, ignoreCase = true) ||
                    stall.category.contains(searchText, ignoreCase = true) ||
                    stall.location.contains(searchText, ignoreCase = true)

        val matchesCategory =
            selectedCategory == "All" ||
                    stall.category.equals(selectedCategory, ignoreCase = true)

        matchesSearch && matchesCategory
    }

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
                filteredStalls.forEach { stall ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(stall.latitude, stall.longitude)
                        ),
                        title = stall.name,
                        snippet = stall.location,
                        onClick = {
                            selectedStall = stall
                            true
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search street food...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp)),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { category ->
                        FilterChip(
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFD8C8FF),
                                selectedLabelColor = Color(0xFF2F247F),
                                containerColor = Color.White
                            ),
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                                selectedStall = filteredStalls.firstOrNull()
                            },
                            label = { Text(category) }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(bangkok, 12f)
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                containerColor = Color.White
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
            }

            if (filteredStalls.isEmpty()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(18.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No stall found")
                    }
                }
            } else {
                selectedStall?.let { stall ->
                    MapPreviewCard(
                        stall = stall,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(
                                start = 18.dp,
                                end = 18.dp,
                                bottom = 90.dp
                            ),
                        onClick = { onStallClick(stall) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MapPreviewCard(
    stall: UserStall,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(18.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F247F))
            ) {
                Text("View")
            }
        }
    }
}