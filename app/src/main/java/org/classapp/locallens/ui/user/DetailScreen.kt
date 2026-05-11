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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.UserStall

private val PurpleBg = Color(0xFFF3EEFF)
private val PurpleMain = Color(0xFF2F247F)

@Composable
fun DetailScreen(
    stall: UserStall,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onDirectionsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color(0xFFE2D8FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(stall.imageEmoji, style = MaterialTheme.typography.displayLarge)

                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                        .padding(8.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-34).dp)
                    .padding(horizontal = 18.dp),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(stall.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("⭐ ${stall.rating}  (${stall.reviewCount} reviews)")

                    Spacer(Modifier.height(14.dp))

                    Text("📍 ${stall.location}")
                    Text("🕒 ${stall.openTime}")
                    Text("💰 ${stall.priceRange}")

                    Spacer(Modifier.height(18.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = onDirectionsClick,
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleMain),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Directions")
                        }

                        OutlinedButton(
                            onClick = onFavoriteToggle,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = if (isFavorite) Color.Red else PurpleMain
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(if (isFavorite) "Saved" else "Save")
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    Text("Menu", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                    stall.menu.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.name)
                            Text(item.price)
                        }
                        HorizontalDivider()
                    }

                    Spacer(Modifier.height(20.dp))

                    Text("Reviews", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3ECFF))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(stall.reviewAuthor, fontWeight = FontWeight.Bold)
                            Text(stall.reviewText)
                            Text("⭐⭐⭐⭐⭐")
                        }
                    }
                }
            }
        }
    }
}