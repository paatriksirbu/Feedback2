package com.example.feedback2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReviewDAO {
    @Insert
    suspend fun insert(review: Review)

    @Query("SELECT * FROM reviews WHERE novelId = :novelId")
    suspend fun getReviewsForNovel(novelId: Int): List<Review>
}
