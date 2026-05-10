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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.UserStall

private val PurpleBg = Color(0xFFF3EEFF)

@Composable
fun FavoriteScreen(
    stalls: List<UserStall>,
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onProfileClick: () -> Unit,
    onStallClick: (UserStall) -> Unit
) {
    Scaffold(
        bottomBar = {
            UserBottomBar(
                selectedTab = UserTab.SAVE,
                onHomeClick = onHomeClick,
                onMapClick = onMapClick,
                onSaveClick = {},
                onProfileClick = onProfileClick
            )
        },
        containerColor = PurpleBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text("Save Places", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("${stalls.size} saved restaurants", color = Color.Gray)

            Spacer(Modifier.height(20.dp))

            if (stalls.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No saved places yet")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(stalls) { stall ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onStallClick(stall) },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(stall.imageEmoji, style = MaterialTheme.typography.headlineMedium)

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(stall.name, fontWeight = FontWeight.Bold)
                                    Text(stall.category, color = Color.Gray)
                                    Text("⭐ ${stall.rating}  •  ${stall.distance}")
                                }

                                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}