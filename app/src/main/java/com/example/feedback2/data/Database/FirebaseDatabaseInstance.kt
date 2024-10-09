package com.example.feedback2.data.Database

import com.google.firebase.database.FirebaseDatabase
object FirebaseDatabaseInstance {

    val instance: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
}