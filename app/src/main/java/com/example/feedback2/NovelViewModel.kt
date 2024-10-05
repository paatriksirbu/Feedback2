package com.example.feedback2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NovelViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : NovelRepository = NovelRepository(application)

    fun insert(novel: Novel) = repository.insert(novel)

    fun delete(novel: Novel) = repository.delete(novel)

    fun getAllNovels(onResult: (List<Novel>) -> Unit) {
        viewModelScope.launch {
            val novels = repository.getAllNovels()
            onResult(novels)
        }
    }
}

