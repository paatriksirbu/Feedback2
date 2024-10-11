package com.example.feedback2.userInterface

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.feedback2.data.Novel
import com.example.feedback2.data.Review
import com.example.feedback2.repository.NovelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NovelViewModel(application: Application, private val repository: NovelRepository) : AndroidViewModel(application) {

    //Para observar la lista de novelas.
    private val _novels = MutableLiveData<List<Novel>>()
    val novels: LiveData<List<Novel>> get() = _novels

    //Para observar el estado de la operacion. (Exito o error)
    private val _operationStatus = MutableLiveData<Result<String>>()
    val operationStatus: LiveData<Result<String>> get() = _operationStatus

    fun insert(novel: Novel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addNovel(novel)
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.success("Novela añadida exitosamente")
                    loadAllNovels()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.failure(e)
                }
            }
        }
    }

    // Eliminar una novela
    fun delete(novelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteNovel(novelId)
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.success("Novela eliminada exitosamente")
                    loadAllNovels()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.failure(e)
                }
            }
        }
    }

    // Obtener todas las novelas desde la base de datos local y actualizar el LiveData
    fun loadAllNovels() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val localNovels = repository.getAllNovelsFromLocal()
                withContext(Dispatchers.Main) {
                    _novels.value = localNovels
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.failure(e)
                }
            }
        }
    }

    // Sincronizar todas las novelas desde Firebase y actualizar la base de datos local
    fun syncNovels() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getAllNovels()
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.success("Sincronización completada")
                    loadAllNovels()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.failure(e)
                }
            }
        }
    }

    // Actualizar una novela existente
    fun updateNovel(novel: Novel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateNovel(novel)
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.success("Novela actualizada exitosamente")
                    loadAllNovels()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.failure(e)
                }
            }
        }
    }

    // Obtener una novela específica
    fun getNovelById(novelId: String, onResult: (Novel?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val novel = repository.getNovelById(novelId)
                withContext(Dispatchers.Main) {
                    onResult(novel)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }

    fun insertReview(review: Review) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertReview(review)
                withContext(Dispatchers.Main) {
                    _operationStatus.value =
                        Result.success("Reseña añadida exitosamente")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _operationStatus.value = Result.failure(e)
                }
            }
        }
    }
}
