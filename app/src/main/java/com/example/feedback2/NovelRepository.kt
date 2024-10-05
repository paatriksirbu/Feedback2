package com.example.feedback2

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NovelRepository (application: Application) {
    private val novelDao: NovelDAO
    private val executorService : ExecutorService = Executors.newFixedThreadPool(2)


    init {
        val database = NovelDatabase.getInstance(application)
        novelDao = database.novelDao()
    }

    fun insert(novel: Novel) {
        executorService.execute{
            novelDao.insert(novel)
        }
    }

    fun delete(novel: Novel){
        executorService.execute{
            novelDao.delete(novel)
        }
    }

    suspend fun getAllNovels(): List<Novel> {
        return withContext(Dispatchers.IO) {
            novelDao.getAllNovels()
        }
    }

}