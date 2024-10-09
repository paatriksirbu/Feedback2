package com.example.feedback2.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.feedback2.repository.NovelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.feedback2.userInterface.NotificationHelper

class SyncDataWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val novelRespository = NovelRepository()

    override suspend fun doWork(): Result {
        return try{
            withContext(Dispatchers.IO){
                syncDataFromFirebase()
            }

            //Mostramos una notificacion al usuario para indicar que se ha completado la sincronizacion.
            NotificationHelper.showSyncCompleteNotification(applicationContext)
            Result.success()
        } catch (e: Exception){
            //En el caso de que algo falle, devolvemos un resultado fallido.
            e.printStackTrace()
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
