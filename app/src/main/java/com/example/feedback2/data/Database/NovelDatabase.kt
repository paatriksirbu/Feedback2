package com.example.feedback2.data.Database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.feedback2.data.Novel
import com.example.feedback2.data.NovelDAO
import com.example.feedback2.data.Review
import com.example.feedback2.data.ReviewDAO

@Database(entities = [Novel::class, Review::class], version = 1)
abstract class NovelDatabase : RoomDatabase() {

    abstract fun novelDao(): NovelDAO
    abstract fun reviewDao(): ReviewDAO

    companion object {
        @Volatile
        private var INSTANCE: NovelDatabase? = null

        fun getInstance(context: Context): NovelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NovelDatabase::class.java,
                    "novel_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}
