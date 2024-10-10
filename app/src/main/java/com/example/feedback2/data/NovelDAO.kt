package com.example.feedback2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NovelDAO {

   @Insert
   fun insert(novel: Novel)

   @Insert
   fun insertAll(novels: List<Novel>)

   @Update
   fun update(novel: Novel)

   @Delete
   fun delete(novel: String)

   @Query("SELECT * FROM novels")
   suspend fun getAllNovels(): List<Novel>

}