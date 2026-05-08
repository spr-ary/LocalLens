package org.classapp.locallens.model

data class ReviewItem(
    val id: String,
    val stallId: String,
    val stallName: String,
    val userName: String,
    val rating: Int,
    val comment: String,
    val createdAt: String
)
