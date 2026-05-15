package com.nammayantra.models

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val village: String = "",
    val district: String = "",
    val role: String = "",
    val createdAt: Long = System.currentTimeMillis()
)