package org.classapp.locallens.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.UserStall
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun DetailScreen(
    stall: UserStall,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FF))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFFE6D9FF)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stall.imageEmoji,
                style = MaterialTheme.typography.displayLarge
            )

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-28).dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(stall.name, style = MaterialTheme.typography.headlineSmall)
                Text("⭐ ${stall.rating}  (${stall.reviewCount} reviews)")

                Spacer(Modifier.height(12.dp))

                Text("📍 ${stall.location}")
                Text("🕒 ${stall.openTime}")
                Text("💰 ${stall.priceRange}")

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { }) {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Directions")
                    }

                    OutlinedButton(onClick = onFavoriteToggle) {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text(if (isFavorite) "Saved" else "Save")
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text("Menu", style = MaterialTheme.typography.titleLarge)

                stall.menu.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.name)
                        Text(item.price)
                    }
                    Divider()
                }

                Spacer(Modifier.height(20.dp))

                Text("Reviews", style = MaterialTheme.typography.titleLarge)

                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3ECFF))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Lisa M.", style = MaterialTheme.typography.titleSmall)
                        Text("Best pad thai near BTS! ❤️")
                        Text("⭐⭐⭐⭐⭐")
                    }
                }
            }
        }
    }
}