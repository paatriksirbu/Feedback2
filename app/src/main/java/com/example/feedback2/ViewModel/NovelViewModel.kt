package com.example.feedback2.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.feedback2.data.Novel
import com.example.feedback2.repository.NovelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NovelViewModel(private val novelRepository: NovelRepository) : ViewModel() {
    //Usamos LiveData para poder ver los resultados de sincronizacion en tiempo real.
    private val _syncStatus = MutableLiveData<Result<String>>()
    val syncStatus: LiveData<Result<String>> = _syncStatus
    //Usamos LiveData para poder ver los datos de la base de datos local en tiempo real en la UI.
    private val _novels = MutableLiveData<List<Novel>>()
    val novels: LiveData<List<Novel>> = _novels

    fun loadAllNovels(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val localNovels = novelRepository.getAllNovelsFromLocal()
                _novels.postValue(localNovels)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Todos los metodos de esta clase actuan sobre la Firebase y la base de datos local.
    //Sincronizamos todas las novelas desde Firebase y actualizamos la base de datos local.
    fun syncNovels() = viewModelScope.launch(Dispatchers.IO){
        try {
            novelRepository.getAllNovels()
            withContext(Dispatchers.Main){
                _syncStatus.value = Result.success("Sincronizacion completada")
            }
        } catch (e: Exception){
            withContext(Dispatchers.Main){
                _syncStatus.value = Result.failure(e)
            }
        }
    }

    fun addNovel(novel: Novel){
        viewModelScope.launch(Dispatchers.IO){
            try {
                novelRepository.addNovel(novel)
                loadAllNovels()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateNovel(novel: Novel){
        viewModelScope.launch(Dispatchers.IO){
            try {
                novelRepository.updateNovel(novel)
                loadAllNovels()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun deleteNovel(novelId: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                novelRepository.deleteNovel(novelId)
                loadAllNovels()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getNovelById(novelId: String) = liveData(Dispatchers.IO){
        try {
            val novel = novelRepository.getNovelById(novelId)
            emit(novel)
        } catch (e: Exception) {
            emit(null)
        }
    }
}