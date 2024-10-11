package com.example.feedback2.userInterface

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.feedback2.repository.NovelRepository

class NovelViewModelFactory (
    private val application: Application,
    private val repository: NovelRepository
): ViewModelProvider.AndroidViewModelFactory(application){
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NovelViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return NovelViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}