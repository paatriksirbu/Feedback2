package com.example.feedback2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "novels")
data class Novel(
    @PrimaryKey(autoGenerate = true)
    var id: String = "",
    val titulo: String,
    val autor: String,
    val year: Int,


)
