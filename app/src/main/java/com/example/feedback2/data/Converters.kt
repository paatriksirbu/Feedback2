package com.example.feedback2.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromReviewList(value: List<Review>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toReviewList(value: String): List<Review> {
        val listType = object : TypeToken<List<Review>>() {}.type
        return Gson().fromJson(value, listType)
    }
}