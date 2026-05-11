package org.classapp.locallens.model

data class UserStall(
    val id: String,
    val name: String,
    val category: String,
    val location: String,
    val openTime: String,
    val priceRange: String,
    val rating: Double,
    val reviewCount: Int,
    val distance: String,
    val imageEmoji: String,
    val menu: List<MenuItem>,
    val latitude: Double,
    val longitude: Double,
    val reviewAuthor: String = "Local User",
    val reviewText: String = "Recommended street food spot!"
)

