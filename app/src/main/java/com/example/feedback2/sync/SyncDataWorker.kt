package com.example.feedback2.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.feedback2.repository.NovelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncDataWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val novelRespository = NovelRepository()

    override suspend fun doWork(): Result {
        return try{
            withContext(Dispatchers.IO){
                syncDataFromFirebase()
            }
            Result.success()
        } catch (e: Exception){
            Result.failure()
        }
    }

    private suspend fun syncDataFromFirebase(){
        val novels = novelRespository.getAllNovels()
        novels.forEach { novel ->
            novelRespository.addNovel(novel)
        }
    }

}
