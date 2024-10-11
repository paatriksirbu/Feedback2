package com.example.feedback2.repository

import android.app.Application
import com.example.feedback2.data.Database.FirebaseDatabaseInstance
import com.example.feedback2.data.Novel
import com.example.feedback2.data.NovelDAO
import com.example.feedback2.data.Database.NovelDatabase
import com.example.feedback2.data.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume


class NovelRepository(private val novelDAO: NovelDAO) {

    private val database : DatabaseReference = FirebaseDatabaseInstance.instance.getReference("novels")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    suspend fun getAllNovels(): List<Novel> = suspendCancellableCoroutine { continuation ->

        val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val novels = snapshot.children.mapNotNull {
                    it.getValue(Novel::class.java)
                }

                try{
                    GlobalScope.launch(Dispatchers.IO) {
                        novelDAO.insertAll(novels)
                        continuation.resume(novels)
                    }
                } catch (e: Exception){
                    continuation.resumeWithException(e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }

    suspend fun addNovel(novel: Novel){
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val novelId = database.push().key ?: throw Exception("Error al generar el ID de la novela")

            novel.userId = userId
            novel.id = novelId.toInt()

            database.child(userId).child(novelId).setValue(novel).await()
            novelDAO.insert(novel)
        } catch (e: Exception){
            e.printStackTrace() //Registrar el error para la depuracion.
            throw e //Lanzamos la excepcion para poder manejarla en la UI
        }
    }

    suspend fun updateNovel(novel: Novel){
        try{
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            if (novel.userId != userId){
                throw Exception("No tienes permiso para editar esta novela")
            }

            //Actualizamos en la base de datos de firebase
            database.child(userId).child(novel.id.toString()).setValue(novel).await()

            //De esta manera actualizamos en la base de datos local
            novelDAO.update(novel)
        } catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }

    suspend fun deleteNovel(novelId: String){
        try{
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")

            //Primero eliminamos de firebase
            database.child(userId).child(novelId).removeValue().await()
            //Posteriormente, eliminamos de la base de datos local
            novelDAO.delete(novelId.toInt())
        } catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getAllNovelsFromLocal(): List<Novel>{
        return try {
            novelDAO.getAllNovels()
        } catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getNovelById(novelId: String): Novel?{
        return try{
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val snapshot = database.child(userId).child(novelId).get().await() //Obtenemos la novela de firebase

            val novel = snapshot.getValue(Novel::class.java)

            novel?.let{ //Si la novela existe, la actualizamos en la base de datos local
                novelDAO.insert(it) //Actualizamos la base de datos local
            }
            novel
        } catch (e: Exception){ //Si la novela no existe o da error, devolvemos null
            e.printStackTrace()
            throw e
        }
    }

    suspend fun insertReview(review: Review) {
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("Usuario no autenticado")
            val reviewId = database.push().key ?: throw Exception("Error al generar el ID de la reseña")

            // Aquí se guarda la reseña en Firebase
            database.child(userId).child("reviews").child(reviewId).setValue(review).await()

            // Y también se guarda en la base de datos local
            novelDAO.insertReview(review)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

}