package com.example.feedback2.userInterface

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.feedback2.R
import com.example.feedback2.data.Database.NovelDatabase
import com.example.feedback2.data.Novel
import com.example.feedback2.databinding.PopupAddNovelBinding
import com.example.feedback2.databinding.PopupDeleteNovelBinding
import com.example.feedback2.databinding.PopupWatchNovelBinding
import com.example.feedback2.repository.NovelRepository
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var buttonAddBook: Button
    private lateinit var listViewNovels: ListView
    private lateinit var novelAdapter: NovelAdapter
    private lateinit var novelViewModel: NovelViewModel
    private lateinit var novelRepository: NovelRepository
    private val novels: MutableList<Novel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = NovelDatabase.getInstance(this)
        novelRepository = NovelRepository(database.novelDao())

        val viewModelFactory = NovelViewModelFactory(application, novelRepository)

        novelViewModel = ViewModelProvider(this, viewModelFactory).get(NovelViewModel::class.java)


        //Configuramos la lista y el adaptador.
        listViewNovels = findViewById(R.id.listViewNovels)
        novelAdapter = NovelAdapter(novels)
        listViewNovels.adapter = novelAdapter

        //Configuracion de los botones.
        val buttonAddNovel = findViewById<Button>(R.id.buttonAddNovel)
        val buttonDeleteNovel = findViewById<Button>(R.id.buttonDeleteNovel)
        val buttonReviews = findViewById<Button>(R.id.buttonReviews)
        val buttonWatchNovels = findViewById<Button>(R.id.buttonWatchNovels)

        setupViewModelObservers()

        novelViewModel.loadAllNovels()


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

    //Configuramos los observadores del ViewModel para poder actualizar la UI
    private fun setupViewModelObservers(){
        //Primero obtenemos la lista de novelas.
        novelViewModel.novels.observe(this) { updatedNovels ->
            novels.clear()
            novels.addAll(updatedNovels)
            novelAdapter.notifyDataSetChanged()
        }

        novelViewModel.operationStatus.observe(this) { result ->
            result.onSuccess{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }.onFailure{
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
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

                    val nuevaNovela = Novel(title = titulo, author = autor, year = year.toInt(), description = "", reviews = emptyList(), userId = "")
                    novelViewModel.insert(nuevaNovela)
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

        //Verificar si hay novelas en la lista para poder eliminar
        if (novels.isEmpty()) {
            Toast.makeText(this, "No hay novelas para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        val listaTitulos = novels.map { "${it.title} (${it.year})" }.toTypedArray()


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
                    novelViewModel.delete(novelaSeleccionada.id.toString())
                    novelAdapter.notifyDataSetChanged()

                    Toast.makeText(this@MainActivity, "Novela '${novelaSeleccionada.title}' eliminada", Toast.LENGTH_SHORT).show()
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
            .setItems(novels.map { it.title }.toTypedArray()) { _, which ->
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
