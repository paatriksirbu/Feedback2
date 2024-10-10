package com.example.feedback2.data

import android.icu.text.CaseMap.Title
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "novels")
data class Novel(
    @PrimaryKey(autoGenerate = true)
    var id: String = "",
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val reviews: List<Review> = emptyList()

)
