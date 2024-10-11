package com.example.feedback2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NovelDAO {

   // Inserta una novela, sin retornar ningún objeto
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insert(novel: Novel)

   // Inserta múltiples novelas, sin retornar ningún objeto
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(novels: List<Novel>)

   // Inserta una reseña, sin retornar ningún objeto
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertReview(review: Review)

   // Obtiene todas las novelas como un flujo de datos
   @Query("SELECT * FROM novels")
   fun getAllNovels(): Flow<List<Novel>>

   // Elimina una novela por su ID, retornando el número de filas eliminadas
   @Query("DELETE FROM novels WHERE id = :novelId")
   suspend fun delete(novelId: Int): Int

   // Obtiene todas las reseñas de una novela por su ID
   @Query("SELECT * FROM reviews WHERE novelId = :novelId")
   suspend fun getReviewsByNovelId(novelId: Int): List<Review>

   // Actualiza una novela
   @Update
   suspend fun update(novel: Novel)
}

