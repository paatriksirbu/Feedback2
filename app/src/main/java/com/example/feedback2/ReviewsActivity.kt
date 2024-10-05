package com.example.feedback2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ReviewsActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var reviewEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var database: NovelDatabase
    private lateinit var spinnerNovels: Spinner

    private val novels: MutableList<Novel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        database = NovelDatabase.getInstance(this)

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
            val rating = ratingBar.rating
            val reviewText = reviewEditText.text.toString()
            val selectedNovelIndex = spinnerNovels.selectedItemPosition

            if (selectedNovelIndex >= 0 && selectedNovelIndex < novels.size) {
                val selectedNovel = novels[selectedNovelIndex]
                val review = Review(novelId = selectedNovel.id, rating = rating, description = reviewText)

                lifecycleScope.launch {
                    try {
                        database.reviewDao().insert(review)

                        Toast.makeText(
                            this@ReviewsActivity,
                            "Reseña enviada: $rating estrellas\n$reviewText",
                            Toast.LENGTH_SHORT
                        ).show()

                        ratingBar.rating = 0f
                        reviewEditText.text.clear()

                        val intent = Intent(this@ReviewsActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@ReviewsActivity, "Error al enviar la reseña: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("SubmitReview", "Error al enviar la reseña", e)
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, selecciona una novela", Toast.LENGTH_SHORT).show()
            }

        }
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

                    val titulos = novels.map { it.titulo }.toTypedArray()
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








