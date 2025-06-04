// file: data/model/SubscriptionModels.kt
package com.example.uijp.data.model

import com.google.gson.annotations.SerializedName

data class PremiumPackageApiResponse(
    @SerializedName("message")
    val message: String?, // Make nullable if it can be absent

    @SerializedName("data")
    val data: List<ApiPremiumPackage>? // List of packages, nullable if it can be absent or empty
)

data class SubscribePackageRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("packageId")
    val packageId: Int,
    @SerializedName("paymentMethod") // Tambahkan jika backend akan menerimanya, walau di-bypass
    val paymentMethod: String? = "bypassed_from_android" // Contoh
)

// Represents a single premium package from the API
data class ApiPremiumPackage(
    @SerializedName("id")
    val id: Int, // Assuming 'id' is always present and an Int

    @SerializedName("package_name")
    val packageName: String?, // Name of the package, nullable

    @SerializedName("duration_monts") // Critical: Matches the "monts" spelling from your API log
    val durationMonths: Int, // Your Kotlin field can be correctly spelled

    @SerializedName("price")
    val price: String?, // Price from API is a String (e.g., "30000"), make nullable

    @SerializedName("description")
    val description: String? // Description, nullable
)

// Represents the overall API response for getting packages
data class GetPremiumPackagesApiResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<ApiPremiumPackage>? // Make data nullable to handle cases where it might be missing
)

// UI specific model - This will be used by your Composable
// It can be derived from ApiPremiumPackage in the ViewModel
data class UiSubscriptionPackage(
    val id: Int, // Unique ID for UI purposes (e.g., index or stable pkg.id)
    val apiId: Int, // Original API ID for subscription requests
    val title: String, // e.g., "Bulanan", "Tahunan"
    val mainPriceDisplay: String, // e.g., "Rp19.000"
    val priceQualifierDisplay: String, // e.g., "/Bulan" or "/Tahun (hemat 20%)"
    val badge: String? = null, // e.g., "Best Value"
    val originalPackage: ApiPremiumPackage // Store the original API package
)

data class SubscriptionData(
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("packageId")
    val packageId: Int?,
    @SerializedName("packageName")
    val packageName: String?,
    @SerializedName("transactionId")
    val transactionId: String?, // Sepertinya backend mengembalikan String untuk ID
    @SerializedName("startDate")
    val startDate: String?,
    @SerializedName("endDate")
    val endDate: String?
)

data class SubscribePackageResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: SubscriptionData?
)

// Untuk getSubscriptionStatus (opsional untuk flow ini, tapi baik untuk dimiliki)
data class UserSubscriptionStatusResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("isPremium")
    val isPremium: Boolean,
    @SerializedName("data")
    val data: SubscriptionData? // Bisa jadi SubscriptionData yang sama atau model lain
)
