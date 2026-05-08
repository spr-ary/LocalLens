package org.classapp.locallens.ui.owner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.classapp.locallens.model.MenuItem
import org.classapp.locallens.model.ReviewItem
import org.classapp.locallens.model.StallProfile
import org.classapp.locallens.ui.owner.components.OwnerBottomBar
import org.classapp.locallens.ui.owner.components.OwnerTopBar
import org.classapp.locallens.ui.owner.screens.DashboardScreen
import org.classapp.locallens.ui.owner.screens.MyStallScreen
import org.classapp.locallens.ui.owner.screens.OwnerProfileScreen
import org.classapp.locallens.ui.owner.screens.ReviewsScreen
import org.classapp.locallens.ui.theme.LocalLensTheme

@Composable
fun StoreOwnerApp() {
    var selectedTab by rememberSaveable { mutableStateOf(OwnerTab.Dashboard) }
    var myStallMode by rememberSaveable { mutableStateOf(MyStallMode.Overview) }
    var ownerName by rememberSaveable { mutableStateOf("Tester") }
    var ownerEmail by rememberSaveable { mutableStateOf("tester@gmail.com") }
    var ownerPhone by rememberSaveable { mutableStateOf("081-234-5678") }
    var selectedStallId by rememberSaveable { mutableStateOf("stall_001") }
    val stalls = remember {
        mutableStateListOf(
            StallProfile(
                id = "stall_001",
                ownerId = "owner_001",
                name = "Nong's Pad Thai",
                description = "Classic wok-fried pad thai near the market with shrimp, tofu, and tamarind sauce.",
                category = "Noodles",
                priceRange = "40-70 THB",
                phone = "081-234-5678",
                address = "Chatuchak Market, Bangkok",
                latitude = "13.7999",
                longitude = "100.5500",
                openingHours = "10:00 - 22:00",
                status = "active",
                averageRating = 4.8,
                totalReviews = 120,
                totalFavorites = 45
            ),
            StallProfile(
                id = "stall_002",
                ownerId = "owner_001",
                name = "Ari Mango Sticky Rice",
                description = "Fresh mango sticky rice and coconut desserts near Ari station.",
                category = "Dessert",
                priceRange = "50-90 THB",
                phone = "081-234-5678",
                address = "Soi Ari 4, Bangkok",
                latitude = "13.7802",
                longitude = "100.5448",
                openingHours = "11:00 - 21:00",
                status = "active",
                averageRating = 4.6,
                totalReviews = 64,
                totalFavorites = 29
            )
        )
    }
    val selectedStall = stalls.firstOrNull { it.id == selectedStallId }
    val menuItems = remember {
        mutableStateListOf(
            MenuItem("Pad Thai", "50", "Original pad thai with tofu and peanuts"),
            MenuItem("Shrimp Pad Thai", "70", "Pad thai with fresh shrimp"),
            MenuItem("Thai iced tea", "25", "Sweet milk tea", available = false)
        )
    }
    val reviews = remember {
        listOf(
            ReviewItem("review_001", "stall_001", "Nong's Pad Thai", "Lisa M.", 5, "Best pad thai near BTS!", "2 days ago"),
            ReviewItem("review_002", "stall_002", "Ari Mango Sticky Rice", "John", 4, "Sweet mango and good coconut milk.", "1 week ago"),
            ReviewItem("review_003", "stall_001", "Nong's Pad Thai", "Mina", 5, "Friendly owner and fresh shrimp.", "2 weeks ago"),
            ReviewItem("review_004", "stall_002", "Ari Mango Sticky Rice", "Arthit", 3, "Nice dessert, wait time can be long.", "1 month ago")
        )
    }

    Scaffold(
        topBar = {
            OwnerTopBar(stallName = selectedStall?.name ?: "Create your stall", isOpen = selectedStall?.isActive == true)
        },
        bottomBar = {
            OwnerBottomBar(
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                    if (it == OwnerTab.MyStall) myStallMode = MyStallMode.Overview
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedTab) {
                OwnerTab.Dashboard -> DashboardScreen(
                    ownerName = ownerName,
                    stalls = stalls,
                    selectedStall = selectedStall,
                    selectedStallId = selectedStallId,
                    totalMenuItems = menuItems.size,
                    onSelectStall = { selectedStallId = it },
                    onAddStall = {
                        selectedTab = OwnerTab.MyStall
                        myStallMode = MyStallMode.Add
                    },
                    onEditStall = {
                        selectedTab = OwnerTab.MyStall
                        myStallMode = MyStallMode.Edit
                    },
                    onAddMenu = {
                        selectedTab = OwnerTab.MyStall
                        myStallMode = MyStallMode.Menu
                    },
                    onManagePhotos = {
                        selectedTab = OwnerTab.MyStall
                        myStallMode = MyStallMode.Photos
                    },
                    onViewReviews = { selectedTab = OwnerTab.Reviews },
                    onPreview = {
                        selectedTab = OwnerTab.MyStall
                        myStallMode = MyStallMode.Preview
                    }
                )

                OwnerTab.MyStall -> MyStallScreen(
                    mode = myStallMode,
                    onModeChange = { myStallMode = it },
                    stalls = stalls,
                    selectedStallId = selectedStallId,
                    onSelectStall = { selectedStallId = it },
                    onStallChange = { updatedStall ->
                        val index = stalls.indexOfFirst { it.id == updatedStall.id }
                        if (index >= 0) {
                            stalls[index] = updatedStall
                        } else {
                            stalls.add(updatedStall)
                        }
                        selectedStallId = updatedStall.id
                    },
                    menuItems = menuItems,
                    onDisableStall = {
                        val index = stalls.indexOfFirst { it.id == selectedStallId }
                        if (index >= 0) stalls[index] = stalls[index].copy(status = "inactive")
                        myStallMode = MyStallMode.Overview
                    }
                )

                OwnerTab.Reviews -> ReviewsScreen(
                    stalls = stalls,
                    selectedStall = selectedStall,
                    selectedStallId = selectedStallId,
                    onSelectStall = { selectedStallId = it },
                    reviews = reviews
                )

                OwnerTab.Profile -> OwnerProfileScreen(
                    ownerName = ownerName,
                    onOwnerNameChange = { ownerName = it },
                    ownerEmail = ownerEmail,
                    onOwnerEmailChange = { ownerEmail = it },
                    ownerPhone = ownerPhone,
                    onOwnerPhoneChange = { ownerPhone = it }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreOwnerAppPreview() {
    LocalLensTheme(dynamicColor = false) {
        StoreOwnerApp()
    }
}
