package org.classapp.locallens.data

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.security.MessageDigest
import java.security.SecureRandom
import kotlinx.coroutines.suspendCancellableCoroutine
import org.classapp.locallens.model.AppUser
import org.classapp.locallens.model.MenuItem
import org.classapp.locallens.model.ReviewItem
import org.classapp.locallens.model.StallProfile
import org.classapp.locallens.model.UserRole
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import org.classapp.locallens.model.UserStall

object FirestoreRepository {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val secureRandom = SecureRandom()

    suspend fun loadUsers(): List<AppUser> {
        return db.collection("users")
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                val roleValue = document.getString("role") ?: return@mapNotNull null
                AppUser(
                    id = document.id,
                    username = document.getString("username").orEmpty(),
                    name = document.getString("name").orEmpty(),
                    phone = document.getString("phone").orEmpty(),
                    role = UserRole.entries.firstOrNull { it.value == roleValue } ?: UserRole.Customer
                )
            }
    }

    fun newUserId(): String = db.collection("users").document().id

    suspend fun loadUser(userId: String): AppUser? {
        val document = db.collection("users").document(userId).get().await()
        if (!document.exists()) return null
        val roleValue = document.getString("role") ?: return null
        return AppUser(
            id = document.id,
            username = document.getString("username").orEmpty(),
            name = document.getString("name").orEmpty(),
            phone = document.getString("phone").orEmpty(),
            role = UserRole.entries.firstOrNull { it.value == roleValue } ?: UserRole.Customer
        )
    }

    suspend fun saveUser(user: AppUser) {
        db.collection("users")
            .document(user.id)
            .set(
                mapOf(
                    "user_id" to user.id,
                    "username" to user.username,
                    "username_lower" to user.username.normalizedUsername(),
                    "name" to user.name,
                    "phone" to user.phone,
                    "role" to user.role.value,
                    "updated_at" to Timestamp.now()
                ),
                SetOptions.merge()
            )
            .await()
    }

    suspend fun createUserProfile(user: AppUser) {
        val now = Timestamp.now()
        db.collection("users")
            .document(user.id)
            .set(
                mapOf(
                    "user_id" to user.id,
                    "username" to user.username,
                    "username_lower" to user.username.normalizedUsername(),
                    "name" to user.name,
                    "phone" to user.phone,
                    "role" to user.role.value,
                    "created_at" to now,
                    "updated_at" to now
                ),
                SetOptions.merge()
            )
            .await()
    }

    suspend fun registerWithUsernamePassword(user: AppUser, password: String): AppUser {
        val usernameLower = user.username.normalizedUsername()
        val existingUser = findUserByUsername(usernameLower)
        if (existingUser != null) error("This username is already registered.")

        val userId = newUserId()
        val salt = newSalt()
        val now = Timestamp.now()
        val userToSave = user.copy(id = userId, username = usernameLower)
        db.collection("users")
            .document(userId)
            .set(
                mapOf(
                    "user_id" to userId,
                    "username" to usernameLower,
                    "username_lower" to usernameLower,
                    "name" to user.name,
                    "phone" to user.phone,
                    "role" to user.role.value,
                    "password_salt" to salt,
                    "password_hash" to hashPassword(password, salt),
                    "created_at" to now,
                    "updated_at" to now
                )
            )
            .await()
        return userToSave
    }

    suspend fun signInWithUsernamePassword(username: String, password: String): AppUser {
        val document = db.collection("users")
            .whereEqualTo("username_lower", username.normalizedUsername())
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull() ?: error("Username or password is incorrect.")

        val salt = document.getString("password_salt").orEmpty()
        val expectedHash = document.getString("password_hash").orEmpty()
        if (salt.isBlank() || expectedHash != hashPassword(password, salt)) {
            error("Username or password is incorrect.")
        }

        val roleValue = document.getString("role") ?: UserRole.Customer.value
        return AppUser(
            id = document.id,
            username = document.getString("username").orEmpty(),
            name = document.getString("name").orEmpty(),
            phone = document.getString("phone").orEmpty(),
            role = UserRole.entries.firstOrNull { it.value == roleValue } ?: UserRole.Customer
        )
    }

    suspend fun loadOwnerStalls(ownerId: String): List<StallProfile> {
        return db.collection("stalls")
            .whereEqualTo("owner_id", ownerId)
            .get()
            .await()
            .documents
            .map { document ->
                StallProfile(
                    id = document.id,
                    ownerId = document.getString("owner_id").orEmpty(),
                    name = document.getString("stall_name").orEmpty(),
                    description = document.getString("description").orEmpty(),
                    category = document.getString("category").orEmpty(),
                    priceRange = document.getString("price_range").orEmpty(),
                    phone = document.getString("phone").orEmpty(),
                    address = document.getString("address").orEmpty(),
                    latitude = document.getDouble("latitude")?.toString().orEmpty(),
                    longitude = document.getDouble("longitude")?.toString().orEmpty(),
                    openingHours = document.getString("opening_hours").orEmpty(),
                    status = document.getString("status") ?: "active",
                    averageRating = document.getDouble("average_rating") ?: 0.0,
                    totalReviews = document.getLong("total_reviews")?.toInt() ?: 0,
                    totalFavorites = document.getLong("total_favorites")?.toInt() ?: 0,
                    coverPhotoUrl = document.getString("cover_photo_url").orEmpty(),
                    photoUrls = (document.get("photo_urls") as? List<*>)?.filterIsInstance<String>().orEmpty()
                )
            }
    }

    fun newStallId(): String = db.collection("stalls").document().id

    suspend fun saveStall(stall: StallProfile) {
        val now = Timestamp.now()
        db.collection("stalls")
            .document(stall.id)
            .set(
                mapOf(
                    "stall_id" to stall.id,
                    "owner_id" to stall.ownerId,
                    "stall_name" to stall.name,
                    "description" to stall.description,
                    "category" to stall.category,
                    "price_range" to stall.priceRange,
                    "phone" to stall.phone,
                    "address" to stall.address,
                    "latitude" to stall.latitude.toDoubleOrNull(),
                    "longitude" to stall.longitude.toDoubleOrNull(),
                    "opening_hours" to stall.openingHours,
                    "cover_photo_url" to stall.coverPhotoUrl,
                    "photo_urls" to stall.photoUrls,
                    "status" to stall.status,
                    "average_rating" to stall.averageRating,
                    "total_reviews" to stall.totalReviews,
                    "total_favorites" to stall.totalFavorites,
                    "updated_at" to now
                ),
                SetOptions.merge()
            )
            .await()
    }

    suspend fun uploadStallPhoto(ownerId: String, stallId: String, imageUri: Uri): String {
        val fileName = "${System.currentTimeMillis()}.jpg"
        val ref = storage.reference
            .child("stall_photos")
            .child(ownerId)
            .child(stallId)
            .child(fileName)
        ref.putFile(imageUri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun loadMenuItems(stallId: String): List<MenuItem> {
        if (stallId.isBlank()) return emptyList()
        return db.collection("menu_items")
            .whereEqualTo("stall_id", stallId)
            .get()
            .await()
            .documents
            .map { document ->
                MenuItem(
                    name = document.getString("menu_name").orEmpty(),
                    price = document.getString("price").orEmpty(),
                    description = document.getString("description").orEmpty(),
                    available = document.getBoolean("is_available") ?: true,
                    id = document.id,
                    stallId = document.getString("stall_id").orEmpty()
                )
            }
    }

    fun newMenuItemId(): String = db.collection("menu_items").document().id

    suspend fun saveMenuItem(item: MenuItem) {
        db.collection("menu_items")
            .document(item.id)
            .set(
                mapOf(
                    "menu_id" to item.id,
                    "stall_id" to item.stallId,
                    "menu_name" to item.name,
                    "price" to item.price,
                    "description" to item.description,
                    "menu_photo_url" to "",
                    "is_available" to item.available,
                    "updated_at" to Timestamp.now()
                ),
                SetOptions.merge()
            )
            .await()
    }

    suspend fun deleteMenuItem(itemId: String) {
        db.collection("menu_items").document(itemId).delete().await()
    }

    suspend fun loadReviews(stallId: String): List<ReviewItem> {
        if (stallId.isBlank()) return emptyList()
        return db.collection("reviews")
            .whereEqualTo("stall_id", stallId)
            .get()
            .await()
            .documents
            .map { document ->
                ReviewItem(
                    id = document.id,
                    stallId = document.getString("stall_id").orEmpty(),
                    stallName = document.getString("stall_name").orEmpty(),
                    userName = document.getString("user_name").orEmpty(),
                    rating = document.getLong("rating")?.toInt() ?: 0,
                    comment = document.getString("comment").orEmpty(),
                    createdAt = "Recently"
                )
            }
    }

    suspend fun loadCustomerStalls(): List<UserStall> {
        val stallDocuments = db.collection("stalls")
            .whereEqualTo("status", "active")
            .get()
            .await()
            .documents

        return stallDocuments.map { document ->
            val stallId = document.getString("stall_id") ?: document.id
            val menuItems = loadMenuItems(stallId)

            UserStall(
                id = stallId,
                name = document.getString("stall_name").orEmpty(),
                category = document.getString("category").orEmpty(),
                location = document.getString("address").orEmpty(),
                openTime = document.getString("opening_hours").orEmpty(),
                priceRange = document.getString("price_range").orEmpty(),
                rating = document.getDouble("average_rating") ?: 0.0,
                reviewCount = document.getLong("total_reviews")?.toInt() ?: 0,
                distance = "Nearby",
                imageEmoji = "🍽️",
                menu = menuItems,
                latitude = document.getDouble("latitude") ?: 13.7563,
                longitude = document.getDouble("longitude") ?: 100.5018,
                reviewAuthor = "Customer",
                reviewText = "Recommended local food stall!"
            )
        }
    }
}

private suspend fun findUserByUsername(usernameLower: String) =
    FirebaseFirestore.getInstance()
        .collection("users")
        .whereEqualTo("username_lower", usernameLower)
        .limit(1)
        .get()
        .await()
        .documents
        .firstOrNull()

private fun String.normalizedUsername(): String = trim().lowercase()

private fun newSalt(): String {
    val bytes = ByteArray(16)
    SecureRandom().nextBytes(bytes)
    return bytes.joinToString("") { "%02x".format(it) }
}

private fun hashPassword(password: String, salt: String): String {
    val input = "$salt:$password".toByteArray(Charsets.UTF_8)
    val digest = MessageDigest.getInstance("SHA-256").digest(input)
    return digest.joinToString("") { "%02x".format(it) }
}

private suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result -> continuation.resume(result) }
        addOnFailureListener { exception -> continuation.resumeWithException(exception) }
    }
}
