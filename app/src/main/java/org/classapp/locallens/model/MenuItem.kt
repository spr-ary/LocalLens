package org.classapp.locallens.model

data class MenuItem(
    val name: String,
    val price: String,
    val description: String,
    val available: Boolean = true
)
