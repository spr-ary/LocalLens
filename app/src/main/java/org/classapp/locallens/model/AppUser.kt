package org.classapp.locallens.model

data class AppUser(
    val id: String,
    val username: String,
    val name: String,
    val phone: String,
    val role: UserRole
)

enum class UserRole(val value: String, val label: String) {
    Customer("customer", "Customer"),
    StallOwner("stall_owner", "Stall Owner")
}
