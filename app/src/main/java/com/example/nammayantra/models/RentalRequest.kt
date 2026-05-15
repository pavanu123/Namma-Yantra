package com.nammayantra.models

data class RentalRequest(
    val requestId: String = "",
    val equipmentId: String = "",
    val equipmentName: String = "",
    val ownerId: String = "",
    val ownerName: String = "",
    val farmerId: String = "",
    val farmerName: String = "",
    val farmerPhone: String = "",
    val rentalType: String = "",
    val duration: Int = 0,
    val totalPrice: Double = 0.0,
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long = System.currentTimeMillis(),
    val status: String = "pending",
    val farmerNote: String = "",
    val createdAt: Long = System.currentTimeMillis()
)