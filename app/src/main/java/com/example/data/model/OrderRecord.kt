package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: String,
    val customerName: String,
    val items: String,
    val price: Int,
    val status: String, // Pending, Preparing, Out for Delivery, Completed
    val timestamp: Long,
    val avatarInitials: String,
    val deliveryAddress: String = "",
    val locationCoords: String = ""
)
