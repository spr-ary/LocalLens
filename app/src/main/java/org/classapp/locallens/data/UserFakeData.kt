package org.classapp.locallens.data

import org.classapp.locallens.model.MenuItem
import org.classapp.locallens.model.UserStall

object UserFakeData {
    val stalls = listOf(
        UserStall(
            id = "1",
            name = "Nong's Pad Thai",
            category = "Noodles",
            location = "Chatuchak Market",
            openTime = "10:00 - 22:00",
            priceRange = "40-60 THB",
            rating = 4.8,
            reviewCount = 120,
            distance = "2.5 km",
            imageEmoji = "🍜",
            menu = listOf(
                MenuItem("Pad Thai", "50 THB"),
                MenuItem("Shrimp Pad Thai", "70 THB")
            ),
            latitude = 13.799,
            longitude = 100.551,
        ),
        UserStall(
            id = "2",
            name = "Ari Mango Sticky Rice",
            category = "Dessert",
            location = "Ari",
            openTime = "11:00 - 20:00",
            priceRange = "50-80 THB",
            rating = 4.6,
            reviewCount = 88,
            distance = "1.8 km",
            imageEmoji = "🥭",
            menu = listOf(
                MenuItem("Mango Sticky Rice", "60 THB"),
                MenuItem("Coconut Ice Cream", "45 THB")
            ),
            latitude = 13.799,
            longitude = 100.551,
        ),
        UserStall(
            id = "3",
            name = "Victory Boat Noodles",
            category = "Noodles",
            location = "Victory Monument",
            openTime = "09:00 - 21:00",
            priceRange = "20-50 THB",
            rating = 4.5,
            reviewCount = 95,
            distance = "3.1 km",
            imageEmoji = "🍲",
            menu = listOf(
                MenuItem("Boat Noodles", "25 THB"),
                MenuItem("Pork Noodles", "35 THB")
            ) ,
            latitude = 13.799,
            longitude = 100.551,
        )
    )
}