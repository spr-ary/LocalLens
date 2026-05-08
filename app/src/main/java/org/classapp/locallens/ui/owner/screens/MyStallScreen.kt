package org.classapp.locallens.ui.owner.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.classapp.locallens.model.MenuItem
import org.classapp.locallens.model.StallProfile
import org.classapp.locallens.ui.owner.MyStallMode
import org.classapp.locallens.ui.owner.components.EmptyStallState
import org.classapp.locallens.ui.owner.components.InfoLine
import org.classapp.locallens.ui.owner.components.ManagementButton
import org.classapp.locallens.ui.owner.components.MenuItemCard
import org.classapp.locallens.ui.owner.components.OwnerTextField
import org.classapp.locallens.ui.owner.components.PhotoStrip
import org.classapp.locallens.ui.owner.components.SectionTitle
import org.classapp.locallens.ui.owner.components.StallSwitcher
import org.classapp.locallens.ui.owner.components.StatusPill

@Composable
fun MyStallScreen(
    mode: MyStallMode,
    onModeChange: (MyStallMode) -> Unit,
    stalls: List<StallProfile>,
    selectedStallId: String,
    onSelectStall: (String) -> Unit,
    onStallChange: (StallProfile) -> Unit,
    menuItems: MutableList<MenuItem>,
    onDisableStall: () -> Unit
) {
    val stall = stalls.firstOrNull { it.id == selectedStallId }

    if (stall == null && mode != MyStallMode.Add) {
        EmptyStallState(
            title = "You have not added your stall yet.",
            body = "Add your stall name, location, menu, hours, and photos.",
            action = "Add Stall",
            onAction = { onModeChange(MyStallMode.Add) }
        )
        return
    }

    when (mode) {
        MyStallMode.Overview -> StallOverview(
            stalls = stalls,
            selectedStallId = selectedStallId,
            stall = stall,
            menuItems = menuItems,
            onSelectStall = onSelectStall,
            onModeChange = onModeChange,
            onDisableStall = onDisableStall
        )

        MyStallMode.Add -> AddOrEditStallForm(
            title = "Add Stall",
            initialStall = null,
            submitText = "Submit",
            onBack = { onModeChange(MyStallMode.Overview) },
            onSubmit = {
                onStallChange(it)
                onModeChange(MyStallMode.Overview)
            }
        )

        MyStallMode.Edit -> AddOrEditStallForm(
            title = "Edit Stall Information",
            initialStall = stall,
            submitText = "Save",
            onBack = { onModeChange(MyStallMode.Overview) },
            onSubmit = {
                onStallChange(it)
                onModeChange(MyStallMode.Overview)
            }
        )

        MyStallMode.Menu -> MenuManagement(
            menuItems = menuItems,
            onBack = { onModeChange(MyStallMode.Overview) }
        )

        MyStallMode.Hours -> HoursManagement(
            stall = stall,
            onBack = { onModeChange(MyStallMode.Overview) },
            onSave = {
                if (stall != null) onStallChange(stall.copy(openingHours = it))
                onModeChange(MyStallMode.Overview)
            }
        )

        MyStallMode.Photos -> PhotosManagement(onBack = { onModeChange(MyStallMode.Overview) })

        MyStallMode.Preview -> CustomerPreview(
            stall = stall,
            menuItems = menuItems,
            onBack = { onModeChange(MyStallMode.Overview) }
        )
    }
}

@Composable
private fun StallOverview(
    stalls: List<StallProfile>,
    selectedStallId: String,
    stall: StallProfile?,
    menuItems: List<MenuItem>,
    onSelectStall: (String) -> Unit,
    onModeChange: (MyStallMode) -> Unit,
    onDisableStall: () -> Unit
) {
    var showDisableDialog by rememberSaveable { mutableStateOf(false) }

    if (stall == null) return

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionTitle("My Stall")
            StallSwitcher(
                stalls = stalls,
                selectedStallId = selectedStallId,
                onSelectStall = onSelectStall,
                onAddStall = { onModeChange(MyStallMode.Add) }
            )
        }
        item {
            StallProfileCard(stall = stall)
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ManagementButton("Stall Information", "Name, category, phone, location", { onModeChange(MyStallMode.Edit) })
                ManagementButton("Menu Management", "${menuItems.size} menu items", { onModeChange(MyStallMode.Menu) })
                ManagementButton("Opening Hours", stall.openingHours, { onModeChange(MyStallMode.Hours) })
                ManagementButton("Photos", "Cover photo and additional photos", { onModeChange(MyStallMode.Photos) })
                ManagementButton("Preview as Customer", "See public detail page", { onModeChange(MyStallMode.Preview) })
            }
        }
        item {
            OutlinedButton(
                onClick = { showDisableDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Disable Stall")
            }
        }
    }

    if (showDisableDialog) {
        AlertDialog(
            onDismissRequest = { showDisableDialog = false },
            title = { Text("Disable stall?") },
            text = { Text("Are you sure you want to disable this stall? Customers will no longer see it.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDisableDialog = false
                        onDisableStall()
                    }
                ) {
                    Text("Disable")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDisableDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AddOrEditStallForm(
    title: String,
    initialStall: StallProfile?,
    submitText: String,
    onBack: () -> Unit,
    onSubmit: (StallProfile) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(initialStall?.name ?: "") }
    var description by rememberSaveable { mutableStateOf(initialStall?.description ?: "") }
    var category by rememberSaveable { mutableStateOf(initialStall?.category ?: "Noodles") }
    var priceRange by rememberSaveable { mutableStateOf(initialStall?.priceRange ?: "40-70 THB") }
    var phone by rememberSaveable { mutableStateOf(initialStall?.phone ?: "") }
    var address by rememberSaveable { mutableStateOf(initialStall?.address ?: "") }
    var latitude by rememberSaveable { mutableStateOf(initialStall?.latitude ?: "") }
    var longitude by rememberSaveable { mutableStateOf(initialStall?.longitude ?: "") }
    var openingHours by rememberSaveable { mutableStateOf(initialStall?.openingHours ?: "10:00 - 22:00") }
    var active by rememberSaveable { mutableStateOf(initialStall?.isActive ?: true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        HeaderWithBack(title = title, onBack = onBack)
        PhotoStrip()
        FormSection("Basic Information")
        OwnerTextField("Stall name", name, { name = it })
        OwnerTextField("Description", description, { description = it }, minLines = 3)
        OwnerTextField("Category", category, { category = it })
        OwnerTextField("Price range", priceRange, { priceRange = it })
        OwnerTextField("Phone number", phone, { phone = it }, KeyboardType.Phone)

        FormSection("Location")
        OwnerTextField("Address", address, { address = it }, minLines = 2)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Latitude") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(8.dp)
            )
            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Longitude") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(8.dp)
            )
        }

        FormSection("Opening Hours")
        OwnerTextField("Opening hours", openingHours, { openingHours = it })
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Active status", fontWeight = FontWeight.Bold)
            Switch(checked = active, onCheckedChange = { active = it })
        }

        FormSection("Preview")
        StallProfileCard(
            stall = (initialStall ?: sampleStall()).copy(
                name = name.ifBlank { "Stall name" },
                description = description,
                category = category.ifBlank { "Category" },
                priceRange = priceRange,
                phone = phone,
                address = address.ifBlank { "Address" },
                latitude = latitude,
                longitude = longitude,
                openingHours = openingHours,
                status = if (active) "active" else "inactive"
            )
        )

        Button(
            onClick = {
                onSubmit(
                    (initialStall ?: sampleStall()).copy(
                        name = name.ifBlank { "New Stall" },
                        description = description,
                        category = category.ifBlank { "Street Food" },
                        priceRange = priceRange,
                        phone = phone,
                        address = address,
                        latitude = latitude,
                        longitude = longitude,
                        openingHours = openingHours,
                        status = if (active) "active" else "inactive"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(submitText)
        }
    }
}

@Composable
private fun MenuManagement(menuItems: MutableList<MenuItem>, onBack: () -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { HeaderWithBack(title = "Menu Management", onBack = onBack) }
        item {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OwnerTextField("Menu name", name, { name = it })
                    OwnerTextField("Price", price, { price = it }, KeyboardType.Number)
                    OwnerTextField("Description", description, { description = it }, minLines = 2)
                    Button(
                        onClick = {
                            if (name.isNotBlank() && price.isNotBlank()) {
                                menuItems.add(MenuItem(name.trim(), price.trim(), description.trim()))
                                name = ""
                                price = ""
                                description = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add Menu Item")
                    }
                }
            }
        }
        items(menuItems, key = { it.name + it.price }) { item ->
            MenuItemCard(
                item = item,
                onToggle = {
                    val index = menuItems.indexOf(item)
                    if (index >= 0) menuItems[index] = item.copy(available = !item.available)
                },
                onRemove = { menuItems.remove(item) }
            )
        }
    }
}

@Composable
private fun HoursManagement(stall: StallProfile?, onBack: () -> Unit, onSave: (String) -> Unit) {
    var hours by rememberSaveable { mutableStateOf(stall?.openingHours ?: "10:00 - 22:00") }
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        HeaderWithBack(title = "Opening Hours", onBack = onBack)
        OwnerTextField("MVP opening hours", hours, { hours = it })
        FormSection("Weekly Template")
        days.forEachIndexed { index, day ->
            Card(shape = RoundedCornerShape(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(day, fontWeight = FontWeight.Bold)
                    Text(if (index == 2 || index == 6) "Closed" else "10:00 - 22:00")
                }
            }
        }
        Button(onClick = { onSave(hours) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Text("Save Hours")
        }
    }
}

@Composable
private fun PhotosManagement(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        HeaderWithBack(title = "Manage Photos", onBack = onBack)
        PhotoStrip()
        ManagementButton("Upload Cover Photo", "Shown on customer cards and dashboard", {})
        ManagementButton("Upload Additional Photos", "Add menu, storefront, and seating photos", {})
        ManagementButton("Set Cover Photo", "Choose the image customers see first", {})
    }
}

@Composable
private fun CustomerPreview(stall: StallProfile?, menuItems: List<MenuItem>, onBack: () -> Unit) {
    if (stall == null) return

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { HeaderWithBack(title = "Preview as Customer", onBack = onBack) }
        item { StallProfileCard(stall = stall) }
        item {
            SectionTitle("Menu")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                menuItems.filter { it.available }.forEach {
                    InfoLine(it.name, "${it.price} THB")
                }
            }
        }
        item {
            SectionTitle("Photos")
            PhotoStrip()
        }
        item {
            SectionTitle("Reviews")
            Text("Owners cannot review their own stall from this preview.")
        }
    }
}

@Composable
private fun StallProfileCard(stall: StallProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFFC7BFFF), Color(0xFFFFE1B5)))),
                contentAlignment = Alignment.Center
            ) {
                Text("Stall Cover Photo", color = Color(0xFF23175F), fontWeight = FontWeight.Bold)
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(stall.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(stall.category)
                }
                StatusPill(isOpen = stall.isActive)
            }
            Text("Rating ${stall.averageRating} (${stall.totalReviews} reviews)", color = MaterialTheme.colorScheme.primary)
            InfoLine("Location", stall.address)
            InfoLine("Opening Hours", stall.openingHours)
            InfoLine("Price Range", stall.priceRange)
        }
    }
}

@Composable
private fun HeaderWithBack(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SectionTitle(title)
        TextButton(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
private fun FormSection(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
}

private fun sampleStall(): StallProfile {
    return StallProfile(
        id = "stall_new",
        ownerId = "owner_001",
        name = "",
        description = "",
        category = "",
        priceRange = "",
        phone = "",
        address = "",
        latitude = "",
        longitude = "",
        openingHours = "",
        status = "active",
        averageRating = 0.0,
        totalReviews = 0,
        totalFavorites = 0
    )
}
