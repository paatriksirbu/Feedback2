package com.example.feedback2.data

import android.icu.text.CaseMap.Title
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "novels")
data class Novel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var author: String,
    var year: Int,
    var description: String,
    @TypeConverters(Converters::class)
    var reviews: List<Review> = emptyList(),
    var userId: String

)