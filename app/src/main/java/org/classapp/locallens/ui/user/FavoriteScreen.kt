package org.classapp.locallens.ui.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.UserStall

@Composable
fun FavoriteScreen(
    stalls: List<UserStall>,
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onStallClick: (UserStall) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onHomeClick,
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onMapClick,
                    icon = { Icon(Icons.Default.LocationOn, null) },
                    label = { Text("Map") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Favorite, null) },
                    label = { Text("Save") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text("Save Places", style = MaterialTheme.typography.headlineSmall)
            Text("${stalls.size} saved restaurants")

            Spacer(Modifier.height(20.dp))

            if (stalls.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No saved places yet")
                }
            } else {
                LazyColumn {
                    items(stalls) { stall ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { onStallClick(stall) },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(stall.imageEmoji, style = MaterialTheme.typography.headlineMedium)
                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(stall.name, style = MaterialTheme.typography.titleMedium)
                                    Text(stall.location)
                                    Text("⭐ ${stall.rating}  |  ${stall.distance}")
                                }

                                Icon(Icons.Default.Favorite, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}