package org.classapp.locallens.ui.owner

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.classapp.locallens.data.FirestoreRepository
import org.classapp.locallens.model.AppUser
import org.classapp.locallens.model.MenuItem
import org.classapp.locallens.model.ReviewItem
import org.classapp.locallens.model.StallProfile
import org.classapp.locallens.model.UserRole
import org.classapp.locallens.ui.owner.components.OwnerBottomBar
import org.classapp.locallens.ui.owner.components.OwnerTopBar
import org.classapp.locallens.ui.owner.screens.DashboardScreen
import org.classapp.locallens.ui.owner.screens.MyStallScreen
import org.classapp.locallens.ui.owner.screens.OwnerProfileScreen
import org.classapp.locallens.ui.owner.screens.ReviewsScreen
import org.classapp.locallens.ui.theme.LocalLensTheme

@Composable
fun StoreOwnerApp(
    owner: AppUser = AppUser(
        id = "owner_001",
        username = "tester",
        name = "Tester",
        phone = "081-234-5678",
        role = UserRole.StallOwner
    ),
    onLogout: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(OwnerTab.Dashboard) }
    var myStallMode by rememberSaveable { mutableStateOf(MyStallMode.Overview) }
    var ownerUsername by rememberSaveable(owner.id) { mutableStateOf(owner.username) }
    var ownerName by rememberSaveable(owner.id) { mutableStateOf(owner.name) }
    var ownerPhone by rememberSaveable(owner.id) { mutableStateOf(owner.phone) }
    var selectedStallId by rememberSaveable(owner.id) { mutableStateOf("") }
    val stalls = remember {
        mutableStateListOf<StallProfile>()
    }
    val selectedStall = stalls.firstOrNull { it.id == selectedStallId }
    val selectedMenuItems = remember { mutableStateListOf<MenuItem>() }
    val reviews = remember { mutableStateListOf<ReviewItem>() }
    var ownerDataError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(owner.id) {
        runCatching {
            FirestoreRepository.loadOwnerStalls(owner.id)
        }.onSuccess { loadedStalls ->
            stalls.clear()
            stalls.addAll(loadedStalls)
            selectedStallId = loadedStalls.firstOrNull()?.id.orEmpty()
            ownerDataError = null
        }.onFailure {
            ownerDataError = "Could not load stalls: ${it.message}"
        }
    }

    LaunchedEffect(selectedStallId) {
        if (selectedStallId.isBlank()) {
            selectedMenuItems.clear()
            reviews.clear()
            return@LaunchedEffect
        }
        runCatching {
            FirestoreRepository.loadMenuItems(selectedStallId)
        }.onSuccess { loadedMenu ->
            selectedMenuItems.clear()
            selectedMenuItems.addAll(loadedMenu)
        }.onFailure {
            ownerDataError = "Could not load menu items: ${it.message}"
        }
        runCatching {
            FirestoreRepository.loadReviews(selectedStallId)
        }.onSuccess { loadedReviews ->
            reviews.clear()
            reviews.addAll(loadedReviews)
        }.onFailure {
            ownerDataError = "Could not load reviews: ${it.message}"
        }
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
                    totalMenuItems = selectedMenuItems.size,
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
                        val normalizedStall = if (updatedStall.id == "stall_new") {
                            updatedStall.copy(
                                id = FirestoreRepository.newStallId(),
                                ownerId = owner.id
                            )
                        } else {
                            updatedStall.copy(ownerId = owner.id)
                        }
                        val index = stalls.indexOfFirst { it.id == normalizedStall.id }
                        if (index >= 0) {
                            stalls[index] = normalizedStall
                        } else {
                            stalls.add(normalizedStall)
                        }
                        selectedStallId = normalizedStall.id
                        scope.launch {
                            runCatching {
                                FirestoreRepository.saveStall(normalizedStall)
                            }.onFailure {
                                ownerDataError = "Could not save stall: ${it.message}"
                            }
                        }
                    },
                    menuItems = selectedMenuItems,
                    onAddMenuItem = { item ->
                        val itemToSave = item.copy(
                            id = FirestoreRepository.newMenuItemId(),
                            stallId = selectedStallId
                        )
                        selectedMenuItems.add(itemToSave)
                        scope.launch {
                            runCatching {
                                FirestoreRepository.saveMenuItem(itemToSave)
                            }.onFailure {
                                ownerDataError = "Could not save menu item: ${it.message}"
                            }
                        }
                    },
                    onToggleMenuItem = { item ->
                        val index = selectedMenuItems.indexOf(item)
                        if (index >= 0) {
                            val updatedItem = item.copy(available = !item.available)
                            selectedMenuItems[index] = updatedItem
                            scope.launch {
                                runCatching {
                                    FirestoreRepository.saveMenuItem(updatedItem)
                                }.onFailure {
                                    ownerDataError = "Could not update menu item: ${it.message}"
                                }
                            }
                        }
                    },
                    onRemoveMenuItem = { item ->
                        selectedMenuItems.remove(item)
                        scope.launch {
                            runCatching {
                                FirestoreRepository.deleteMenuItem(item.id)
                            }.onFailure {
                                ownerDataError = "Could not delete menu item: ${it.message}"
                            }
                        }
                    },
                    onUploadCoverPhoto = { imageUri ->
                        val stall = selectedStall ?: return@MyStallScreen
                        scope.launch {
                            runCatching {
                                val photoUrl = FirestoreRepository.uploadStallPhoto(stall.ownerId, stall.id, imageUri)
                                val updatedStall = stall.copy(coverPhotoUrl = photoUrl)
                                FirestoreRepository.saveStall(updatedStall)
                                updatedStall
                            }.onSuccess { updatedStall ->
                                val index = stalls.indexOfFirst { it.id == updatedStall.id }
                                if (index >= 0) stalls[index] = updatedStall
                            }.onFailure {
                                ownerDataError = "Could not upload cover photo: ${it.message}"
                            }
                        }
                    },
                    onUploadAdditionalPhoto = { imageUri ->
                        val stall = selectedStall ?: return@MyStallScreen
                        scope.launch {
                            runCatching {
                                val photoUrl = FirestoreRepository.uploadStallPhoto(stall.ownerId, stall.id, imageUri)
                                val updatedStall = stall.copy(photoUrls = stall.photoUrls + photoUrl)
                                FirestoreRepository.saveStall(updatedStall)
                                updatedStall
                            }.onSuccess { updatedStall ->
                                val index = stalls.indexOfFirst { it.id == updatedStall.id }
                                if (index >= 0) stalls[index] = updatedStall
                            }.onFailure {
                                ownerDataError = "Could not upload photo: ${it.message}"
                            }
                        }
                    },
                    onDisableStall = {
                        val index = stalls.indexOfFirst { it.id == selectedStallId }
                        if (index >= 0) {
                            val disabledStall = stalls[index].copy(status = "inactive")
                            stalls[index] = disabledStall
                            scope.launch {
                                runCatching {
                                    FirestoreRepository.saveStall(disabledStall)
                                }.onFailure {
                                    ownerDataError = "Could not disable stall: ${it.message}"
                                }
                            }
                        }
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
                    ownerUsername = ownerUsername,
                    onOwnerUsernameChange = { ownerUsername = it },
                    ownerName = ownerName,
                    onOwnerNameChange = { ownerName = it },
                    ownerPhone = ownerPhone,
                    onOwnerPhoneChange = { ownerPhone = it },
                    onSaveProfile = {
                        scope.launch {
                            runCatching {
                                FirestoreRepository.saveUser(
                                    owner.copy(
                                        username = ownerUsername,
                                        name = ownerName,
                                        phone = ownerPhone
                                    )
                                )
                            }.onFailure {
                                ownerDataError = "Could not save profile: ${it.message}"
                            }
                        }
                    },
                    onLogout = onLogout
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
