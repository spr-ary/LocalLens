package org.classapp.locallens.ui.user

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.UserStall

@Composable
fun HomeScreen(
    stalls: List<UserStall>,
    favorites: List<String>,
    onStallClick: (UserStall) -> Unit,
    onMapClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {

    Scaffold(

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
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
                    selected = false,
                    onClick = onFavoriteClick,
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
                .padding(16.dp)
        ) {

            Text(
                text = "Hello, User",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Discover Local Street Food Near You",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                CategoryChip("Noodles")
                CategoryChip("Rice")
                CategoryChip("Dessert")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Popular Nearby",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stall.imageEmoji,
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {

                                Text(
                                    text = stall.name,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = stall.category,
                                    color = Color.Gray
                                )

                                Text(
                                    text = "${stall.rating} ★  |  ${stall.distance}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(text: String) {

    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFE9DFFF),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text)
    }
}