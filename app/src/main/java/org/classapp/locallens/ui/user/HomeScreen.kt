package org.classapp.locallens.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.UserStall

private val PurpleBg = Color(0xFFF3EEFF)
private val PurpleMain = Color(0xFF2F247F)
private val PurpleCard = Color(0xFFE7DFF5)

@Composable
fun HomeScreen(
    userName: String,
    stalls: List<UserStall>,
    favorites: List<String>,
    onStallClick: (UserStall) -> Unit,
    onMapClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSeeAllClick: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Noodles", "Rice", "Dessert", "Drinks", "Healthy", "BBQ")

    val filteredStalls = stalls.filter { stall ->
        val matchSearch =
            stall.name.contains(searchText, ignoreCase = true) ||
                    stall.category.contains(searchText, ignoreCase = true) ||
                    stall.location.contains(searchText, ignoreCase = true)

        val matchCategory =
            selectedCategory == "All" || stall.category.equals(selectedCategory, ignoreCase = true)

        matchSearch && matchCategory
    }

    Scaffold(
        bottomBar = {
            UserBottomBar(
                selectedTab = UserTab.HOME,
                onHomeClick = {},
                onMapClick = onMapClick,
                onSaveClick = onFavoriteClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = PurpleBg
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE4DAFF))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("Hello, $userName")
                        Text(
                            "📍 Discover Local Street Food Near You",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(Modifier.height(14.dp))

                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Search street food...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    "Popular Categories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PurpleMain
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterCategoryBox("🍽️", "All", selectedCategory, Modifier.weight(1f)) {
                            selectedCategory = "All"
                        }
                        FilterCategoryBox("🍜", "Noodles", selectedCategory, Modifier.weight(1f)) {
                            selectedCategory = "Noodles"
                        }
                        FilterCategoryBox("🍚", "Rice", selectedCategory, Modifier.weight(1f)) {
                            selectedCategory = "Rice"
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterCategoryBox("🍨", "Dessert", selectedCategory, Modifier.weight(1f)) {
                            selectedCategory = "Dessert"
                        }
                        FilterCategoryBox("🥤", "Drinks", selectedCategory, Modifier.weight(1f)) {
                            selectedCategory = "Drinks"
                        }
                        FilterCategoryBox("🍢", "BBQ", selectedCategory, Modifier.weight(1f)) {
                            selectedCategory = "BBQ"
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        if (searchText.isBlank() && selectedCategory == "All")
                            "Popular Nearby"
                        else
                            "${filteredStalls.size} result(s)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PurpleMain
                    )

                    if (searchText.isNotBlank() || selectedCategory != "All") {
                        Text(
                            "Clear",
                            color = PurpleMain,
                            modifier = Modifier.clickable {
                                onSeeAllClick()
                            }
                        )
                    } else {
                        Text(
                            "See all ›",
                            color = PurpleMain,
                            modifier = Modifier.clickable {
                                searchText = ""
                                selectedCategory = "All"
                            }
                        )
                    }
                }
            }

            if (filteredStalls.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No street food found 😭")
                    }
                }
            } else {
                items(filteredStalls) { stall ->
                    StallCard(
                        stall = stall,
                        isFavorite = favorites.contains(stall.id),
                        onClick = { onStallClick(stall) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterCategoryBox(
    icon: String,
    title: String,
    selectedCategory: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val selected = selectedCategory == title

    Card(
        modifier = modifier
            .height(76.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(0xFFD8C8FF) else Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, style = MaterialTheme.typography.titleLarge)
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun StallCard(
    stall: UserStall,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PurpleCard),
                contentAlignment = Alignment.Center
            ) {
                Text(stall.imageEmoji, style = MaterialTheme.typography.headlineMedium)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(stall.name, fontWeight = FontWeight.Bold)
                Text(stall.category, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Text("⭐ ${stall.rating}  •  ${stall.distance}", style = MaterialTheme.typography.bodySmall)
            }

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = if (isFavorite) Color.Red else Color.LightGray
            )
        }
    }
}