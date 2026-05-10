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
import androidx.compose.runtime.Composable
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
    stalls: List<UserStall>,
    favorites: List<String>,
    onStallClick: (UserStall) -> Unit,
    onMapClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onProfileClick: () -> Unit
) {
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
                        Text("Hello, User", fontWeight = FontWeight.Bold)
                        Text("📍 Discover Local Street Food Near You", style = MaterialTheme.typography.bodySmall)

                        Spacer(Modifier.height(14.dp))

                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
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
                        CategoryBox("🍜", "Noodles", Modifier.weight(1f))
                        CategoryBox("🍚", "Rice", Modifier.weight(1f))
                        CategoryBox("🍨", "Dessert", Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        CategoryBox("🥤", "Drinks", Modifier.weight(1f))
                        CategoryBox("🥗", "Healthy", Modifier.weight(1f))
                        CategoryBox("🍢", "BBQ", Modifier.weight(1f))
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Popular Nearby",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PurpleMain
                    )
                    Text("See all ›", color = PurpleMain)
                }
            }

            items(stalls) { stall ->
                StallCard(
                    stall = stall,
                    isFavorite = favorites.contains(stall.id),
                    onClick = { onStallClick(stall) }
                )
            }
        }
    }
}

@Composable
private fun CategoryBox(icon: String, title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, style = MaterialTheme.typography.titleLarge)
            Text(title, style = MaterialTheme.typography.bodySmall)
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