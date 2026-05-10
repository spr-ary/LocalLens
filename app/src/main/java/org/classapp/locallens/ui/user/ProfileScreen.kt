package org.classapp.locallens.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PurpleBg = Color(0xFFF3EEFF)

@Composable
fun ProfileScreen(
    savedCount: Int,
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            UserBottomBar(
                selectedTab = UserTab.PROFILE,
                onHomeClick = onHomeClick,
                onMapClick = onMapClick,
                onSaveClick = onFavoriteClick,
                onProfileClick = {}
            )
        },
        containerColor = PurpleBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE4DAFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", style = MaterialTheme.typography.displaySmall)
            }

            Spacer(Modifier.height(12.dp))

            Text("Guest User", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Food explorer in Bangkok", color = Color.Gray)

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Saved", savedCount.toString())
                StatCard("Reviews", "0")
                StatCard("Visited", "3")
            }

            Spacer(Modifier.height(24.dp))

            ProfileOption("❤️ Favorite street food", "Noodles, Rice, Dessert")
            ProfileOption("📍 Current city", "Bangkok, Thailand")
            ProfileOption("🌐 Language", "English / Thai")
            ProfileOption("ℹ️ About LocalLens", "Discover local street food near you")
        }
    }
}

@Composable
private fun StatCard(title: String, value: String) {
    Card(
        modifier = Modifier.width(95.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ProfileOption(title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color.Gray)
        }
    }
}