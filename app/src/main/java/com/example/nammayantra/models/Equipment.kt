package com.nammayantra.models

data class Equipment(
    val equipmentId: String = "",
    val ownerId: String = "",
    val ownerName: String = "",
    val name: String = "",
    val type: String = "",
    val description: String = "",
    val hourlyRate: Double = 0.0,
    val dailyRate: Double = 0.0,
    val location: String = "",
    val village: String = "",
    val district: String = "",
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)