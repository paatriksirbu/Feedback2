package com.example.feedback2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NovelDAO {

   @Insert
   fun insert(novel: Novel)

   @Delete
   fun delete(novel: Novel)

   @Query("SELECT * FROM novels")
   suspend fun getAllNovels(): List<Novel>

}