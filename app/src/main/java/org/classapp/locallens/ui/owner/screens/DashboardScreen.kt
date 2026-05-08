package org.classapp.locallens.ui.owner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.StallProfile
import org.classapp.locallens.ui.owner.components.EmptyStallState
import org.classapp.locallens.ui.owner.components.ManagementButton
import org.classapp.locallens.ui.owner.components.SectionTitle
import org.classapp.locallens.ui.owner.components.SoftStatCard
import org.classapp.locallens.ui.owner.components.StallSwitcher
import org.classapp.locallens.ui.owner.components.StallSummaryCard

@Composable
fun DashboardScreen(
    ownerName: String,
    stalls: List<StallProfile>,
    selectedStall: StallProfile?,
    selectedStallId: String,
    totalMenuItems: Int,
    onSelectStall: (String) -> Unit,
    onAddStall: () -> Unit,
    onEditStall: () -> Unit,
    onAddMenu: () -> Unit,
    onManagePhotos: () -> Unit,
    onViewReviews: () -> Unit,
    onPreview: () -> Unit
) {
    if (selectedStall == null) {
        EmptyStallState(
            title = "You have not added your stall yet.",
            body = "Create a stall profile so customers can discover your food on LocalLens.",
            action = "Add Stall",
            onAction = onAddStall
        )
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Dashboard", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Hello, $ownerName", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            StallSwitcher(
                stalls = stalls,
                selectedStallId = selectedStallId,
                onSelectStall = onSelectStall,
                onAddStall = onAddStall
            )
        }
        item {
            StallSummaryCard(stall = selectedStall)
        }
        item {
            SectionTitle("Summary")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SoftStatCard("Menu Items", totalMenuItems.toString(), Modifier.weight(1f))
                SoftStatCard("Favorites", selectedStall.totalFavorites.toString(), Modifier.weight(1f))
                SoftStatCard("Reviews", selectedStall.totalReviews.toString(), Modifier.weight(1f))
            }
        }
        item {
            SectionTitle("Quick Actions")
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ManagementButton("Edit Stall", "Basic info", onEditStall, Modifier.weight(1f))
                    ManagementButton("Add Menu", "Food item", onAddMenu, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ManagementButton("Manage Photos", "Cover and gallery", onManagePhotos, Modifier.weight(1f))
                    ManagementButton("View Reviews", "Customer feedback", onViewReviews, Modifier.weight(1f))
                }
                Button(
                    onClick = onPreview,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Preview as Customer")
                }
            }
        }
    }
}
