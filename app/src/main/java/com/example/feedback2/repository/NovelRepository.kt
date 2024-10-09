package com.example.feedback2.repository

import android.app.Application
import com.example.feedback2.data.Database.FirebaseDatabaseInstance
import com.example.feedback2.data.Novel
import com.example.feedback2.data.NovelDAO
import com.example.feedback2.data.Database.NovelDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume


class NovelRepository {

    private val database : DatabaseReference = FirebaseDatabaseInstance.instance.getReference("novels")


    suspend fun getAllNovels(): List<Novel> = suspendCancellableCoroutine { continuation ->
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val novels = snapshot.children.mapNotNull {
                    it.getValue(Novel::class.java)
                }
                continuation.resume(novels)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())

            }
        })
    }

    suspend fun addNovel(novel: Novel){
        val novelId = database.push().key ?: return
        novel.id = novelId
        database.child(novelId).setValue(novel).await()
    }

    suspend fun updateNovel(novel: Novel){
        database.child(novel.id).setValue(novel).await()
    }

    suspend fun deleteNovel(id: String){
        database.child(id).removeValue().await()
    }


}