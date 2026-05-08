package org.classapp.locallens.ui.owner.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.ReviewItem
import org.classapp.locallens.model.StallProfile
import org.classapp.locallens.ui.owner.components.EmptyStallState
import org.classapp.locallens.ui.owner.components.SectionTitle
import org.classapp.locallens.ui.owner.components.StallSwitcher

@Composable
fun ReviewsScreen(
    stalls: List<StallProfile>,
    selectedStall: StallProfile?,
    selectedStallId: String,
    onSelectStall: (String) -> Unit,
    reviews: List<ReviewItem>
) {
    if (selectedStall == null) {
        EmptyStallState(
            title = "No reviews yet.",
            body = "Create your stall first. Customer reviews will appear here.",
            action = "Reviews",
            onAction = {}
        )
        return
    }

    var selectedRating by rememberSaveable { mutableStateOf(0) }
    var newestFirst by rememberSaveable { mutableStateOf(true) }
    val visibleReviews = reviews
        .filter { it.stallId == selectedStall.id }
        .filter { selectedRating == 0 || it.rating == selectedRating }
        .let { if (newestFirst) it else it.reversed() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionTitle("Reviews")
        }
        item {
            StallSwitcher(
                stalls = stalls,
                selectedStallId = selectedStallId,
                onSelectStall = onSelectStall,
                onAddStall = {}
            )
        }
        item {
            RatingSummary(stall = selectedStall)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0, 5, 4, 3, 2, 1).forEach { rating ->
                    FilterChip(
                        selected = selectedRating == rating,
                        onClick = { selectedRating = rating },
                        label = { Text(if (rating == 0) "All" else rating.toString()) }
                    )
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = newestFirst,
                    onClick = { newestFirst = true },
                    label = { Text("Newest") }
                )
                FilterChip(
                    selected = !newestFirst,
                    onClick = { newestFirst = false },
                    label = { Text("Oldest") }
                )
            }
        }
        items(visibleReviews, key = { it.id }) { review ->
            ReviewCard(review = review)
        }
        if (visibleReviews.isEmpty()) {
            item {
                Text(
                    text = "No reviews match this filter for ${selectedStall.name}.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RatingSummary(stall: StallProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(stall.averageRating.toString(), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text("Average rating")
            }
            Column {
                Text(stall.totalReviews.toString(), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text("Total reviews")
            }
        }
    }
}

@Composable
private fun ReviewCard(review: ReviewItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(review.userName, fontWeight = FontWeight.Bold)
                Text(review.createdAt, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(review.stallName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Rating ${review.rating}/5", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(review.comment)
        }
    }
}
