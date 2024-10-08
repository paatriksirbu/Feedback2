package com.example.feedback2

import android.content.Context
import android.content.Intent
import com.example.feedback2.userInterface.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncDataTask(private val context: Context) {


    fun execute() {
        CoroutineScope(Dispatchers.IO).launch {

            syncDataFromServer()

            //Implementar logica para sincronizar datos con el servidor

            launch(Dispatchers.Main) {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    private suspend fun syncDataFromServer() {

    }
}
