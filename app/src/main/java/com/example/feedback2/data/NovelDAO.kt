package com.example.feedback2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NovelDAO {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insert(novel: Novel)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertAll(novels: List<Novel>)

   @Update
   fun update(novel: Novel)

   @Delete
   fun delete(novel: String)

   @Query("SELECT * FROM novels")
   suspend fun getAllNovels(): List<Novel>

}