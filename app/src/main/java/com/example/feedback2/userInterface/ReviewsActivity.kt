package com.example.feedback2.userInterface

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.feedback2.R
import com.example.feedback2.data.Novel
import com.example.feedback2.data.Database.NovelDatabase
import com.example.feedback2.data.Review
import kotlinx.coroutines.launch

class ReviewsActivity : AppCompatActivity() {

    private val novelViewModel: NovelViewModel by viewModels{
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private lateinit var ratingBar: RatingBar
    private lateinit var reviewEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var database: NovelDatabase
    private lateinit var spinnerNovels: Spinner

    private val novels: MutableList<Novel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        setupViewModelObservers()

        ratingBar = findViewById(R.id.ratingBar)
        reviewEditText = findViewById(R.id.reviewEditText)
        submitButton = findViewById(R.id.submitButton)
        spinnerNovels = findViewById(R.id.spinnerNovels)

        lifecycleScope.launch{
            loadNovels()
        }

        val novelId = intent.getIntExtra("novelId", -1)
        if (novelId == -1) {
            Toast.makeText(this,"No se pudo obtener el ID de la novela", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        submitButton.setOnClickListener {
            submitReview()
        }
    }

    private fun submitReview() {
        val rating = ratingBar.rating
        val reviewText = reviewEditText.text.toString()
        val selectedNovelIndex = spinnerNovels.selectedItemPosition

        if (selectedNovelIndex >= 0 && selectedNovelIndex < novels.size) {
            val selectedNovel = novels[selectedNovelIndex]
            val novelId = selectedNovel.id.toInt()

            val review = Review(
                novelId = novelId,
                rating = rating,
                description = reviewText
            )

            novelViewModel.insertReview(review)

            // Limpiar los campos de la UI después de enviar la reseña
            ratingBar.rating = 0f
            reviewEditText.text.clear()

            // Navegar de vuelta a la actividad principal después de enviar la reseña
            val intent = Intent(this@ReviewsActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        } else {
            Toast.makeText(this, "Por favor, selecciona una novela", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModelObservers() {
        // Observar la lista de novelas
        novelViewModel.novels.observe(this, Observer { updatedNovels ->
            novels.clear()
            novels.addAll(updatedNovels)
            updateSpinner()
        })

        // Observar el estado de las operaciones para mostrar mensajes al usuario
        novelViewModel.operationStatus.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.e("ReviewActivity", "Error en operación: ${it.message}", it)
            }
        })

        // Cargar las novelas al iniciar
        novelViewModel.loadAllNovels()
    }

    private fun updateSpinner() {
        val titles = novels.map { it.title }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNovels.adapter = adapter

    }
    private fun loadNovels(){
        lifecycleScope.launch {
            try {
                novels.clear()
                val novelsFromDb = database.novelDao().getAllNovels()
                Log.d("Cargar novelas","Novelas desde la base de datos: $novelsFromDb")
                if (novelsFromDb.isNullOrEmpty()) {
                    Log.e("Cargar novelas", "No hay novelas en la base de datos")
                    Toast.makeText(this@ReviewsActivity, "No hay novelas disponibles", Toast.LENGTH_SHORT).show()
                } else {
                    novels.addAll(novelsFromDb)
                    Log.d("Cargar novelas", "Novelas cargadas: $novels")

                    val titulos = novels.map { it.title }.toTypedArray()
                    val adapter = ArrayAdapter(this@ReviewsActivity, android.R.layout.simple_spinner_item, titulos)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerNovels.adapter = adapter
                }
            } catch (e: Exception) {
                Toast.makeText(this@ReviewsActivity, "Error al cargar las novelas: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("LoadNovels", "Error al cargar las novelas", e)
            }
        }

    }
}








