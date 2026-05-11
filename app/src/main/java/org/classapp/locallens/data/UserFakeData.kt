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
            reviewAuthor = "Pim",
            reviewText = "Best pad thai in bkk"

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
            reviewAuthor = "Mint",
            reviewText = "Sweet mango and soft sticky rice. Very good!"
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
            reviewAuthor = "Jane",
            reviewText = "I love the noodle!"
        ),

        UserStall(
            id = "4",
            name = "Siam Pork Skewers",
            category = "BBQ",
            location = "Siam Square",
            openTime = "16:00 - 23:00",
            priceRange = "10-50 THB",
            rating = 4.7,
            reviewCount = 76,
            distance = "1.2 km",
            imageEmoji = "🍢",
            menu = listOf(
                MenuItem("Pork Skewer", "15 THB"),
                MenuItem("Sticky Rice", "10 THB")
            ),
            latitude = 13.7456,
            longitude = 100.5348,
            reviewAuthor = "Kale",
            reviewText = "Very good pork, best BBQ!"
        ),
        UserStall(
            id = "5",
            name = "Yaowarat Grilled Squid",
            category = "BBQ",
            location = "Yaowarat Road",
            openTime = "17:00 - 00:00",
            priceRange = "60-120 THB",
            rating = 4.9,
            reviewCount = 210,
            distance = "4.4 km",
            imageEmoji = "🦑",
            menu = listOf(
                MenuItem("Grilled Squid", "100 THB"),
                MenuItem("Seafood Sauce", "10 THB")
            ),
            latitude = 13.7407,
            longitude = 100.5088,
            reviewAuthor = "rosy",
            reviewText = "my favorite grilled squidd"
        ),
        UserStall(
            id = "6",
            name = "Ari Thai Tea",
            category = "Drinks",
            location = "Ari",
            openTime = "09:00 - 18:00",
            priceRange = "25-60 THB",
            rating = 4.4,
            reviewCount = 54,
            distance = "2.0 km",
            imageEmoji = "🧋",
            menu = listOf(
                MenuItem("Thai Tea", "35 THB"),
                MenuItem("Lemon Tea", "30 THB")
            ),
            latitude = 13.7797,
            longitude = 100.5448,
            reviewAuthor = "Milly",
            reviewText = "Best Thai tea in town, everyone should try"
        )
    )
}