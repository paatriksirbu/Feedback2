package com.example.feedback2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val novelId: Int,
    val rating: Float,
    val description: String?
)
