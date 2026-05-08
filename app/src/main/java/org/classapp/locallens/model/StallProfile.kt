package org.classapp.locallens.model

data class StallProfile(
    val id: String,
    val ownerId: String,
    val name: String,
    val description: String,
    val category: String,
    val priceRange: String,
    val phone: String,
    val address: String,
    val latitude: String,
    val longitude: String,
    val openingHours: String,
    val status: String,
    val averageRating: Double,
    val totalReviews: Int,
    val totalFavorites: Int
) {
    val isActive: Boolean
        get() = status == "active"
}
