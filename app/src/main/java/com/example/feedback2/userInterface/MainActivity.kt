package com.example.feedback2.userInterface

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback2.R
import com.example.feedback2.data.Novel
import com.example.feedback2.databinding.PopupAddNovelBinding
import com.example.feedback2.databinding.PopupDeleteNovelBinding
import com.example.feedback2.databinding.PopupWatchNovelBinding
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var buttonAddBook: Button
    private lateinit var listViewNovels: ListView
    private lateinit var novelAdapter: NovelAdapter
    private lateinit var novelViewModel: NovelViewModel
    private val novels: MutableList<Novel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewNovels = findViewById(R.id.listViewNovels)
        novelAdapter = NovelAdapter(novels)
        listViewNovels.adapter = novelAdapter


        val buttonAddNovel = findViewById<Button>(R.id.buttonAddNovel)
        val buttonDeleteNovel = findViewById<Button>(R.id.buttonDeleteNovel)
        val buttonReviews = findViewById<Button>(R.id.buttonReviews)
        val buttonWatchNovels = findViewById<Button>(R.id.buttonWatchNovels)


       buttonAddNovel.setOnClickListener {
           showPopupAddNovel()
       }

        buttonDeleteNovel.setOnClickListener{
            showPopupDeleteNovel()
        }

        buttonReviews.setOnClickListener{
            showPopupReviews()
        }

        buttonWatchNovels.setOnClickListener{
            showPopupWatchNovels()
        }

    }

    private fun showPopupAddNovel() {
        val binding = PopupAddNovelBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this).apply {
            setView(binding.root)
            setTitle("Agregar Novela")
            setPositiveButton("Agregar") { dialog, _ ->
                val titulo = binding.editTextTitulo.text.toString()
                val autor = binding.editTextAutor.text.toString()
                val year = binding.editTextYear.text.toString()

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)

                if (titulo.isNotEmpty() && autor.isNotEmpty() && year.isNotEmpty()) {

                    if (year.toInt() > currentYear) {
                        Toast.makeText(this@MainActivity, "El año de publicación no puede ser mayor al año actual", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val nuevaNovela = Novel(titulo = titulo, autor = autor, year = year.toInt())
                    novels.add(nuevaNovela)
                    novelAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
        }.create()
        dialog.show()
    }

    private fun showPopupDeleteNovel() {
        val binding = PopupDeleteNovelBinding.inflate(layoutInflater)

        //Verificamos si hay novelas en la lista para poder eliminar
        if (novels.isEmpty()) {
            Toast.makeText(this, "No hay novelas para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        val listaTitulos = novels.map { "${it.titulo} (${it.year})" }.toTypedArray()


        val dialog = AlertDialog.Builder(this).apply {
            setView(binding.root)
            setTitle("Eliminar Novela")

            // Configurar el Spinner con los títulos de las novelas
            binding.spinnerNovelas.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, listaTitulos)

            setPositiveButton("Eliminar") { dialog, _ ->
                val selectedIndex = binding.spinnerNovelas.selectedItemPosition

                if (selectedIndex >= 0) {
                    // Eliminar la novela seleccionada
                    val novelaSeleccionada = novels[selectedIndex]
                    novels.removeAt(selectedIndex)
                    novelAdapter.notifyDataSetChanged()

                    Toast.makeText(this@MainActivity, "Novela '${novelaSeleccionada.titulo}' eliminada", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }

            setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
        }.create()
        dialog.show()
    }

    private fun showPopupReviews() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Seleccionar Novela")
            .setItems(novels.map { it.titulo }.toTypedArray()) { _, which ->
                // Obtener el ID de la novela seleccionada
                val selectedNovel = novels[which]
                val intent = Intent(this, ReviewsActivity::class.java).apply {
                    putExtra("novelId", selectedNovel.id) // Pasamos el ID de la novela
                }
                startActivity(intent) // Iniciar la ReviewsActivity
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun showPopupWatchNovels() {
        val binding = PopupWatchNovelBinding.inflate(layoutInflater)

        binding.listViewPopupNovelas.adapter = novelAdapter

        val dialog = AlertDialog.Builder(this).apply {
            setView(binding.root)
            setTitle("Lista de Novelas")
            setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
        }.create()

        dialog.show()
    }
}
