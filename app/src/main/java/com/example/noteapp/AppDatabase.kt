package com.example.noteapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.jetbrains.anko.doAsync

@Database(entities = [Note::class, Category::class],version = 4, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract fun noteDao():NoteDao
    abstract fun categoryDao():CategoryDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "note.db")
            .fallbackToDestructiveMigration()
            .addCallback(object : Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    doAsync {
                        var categorias = arrayListOf(
                            Category(0, "Viajes"),
                            Category(0, "Personal"),
                            Category(0, "Tiempo libre"),
                            Category(0, "Ocio")
                        )
                        instance?.categoryDao()?.saveCategories(categorias)
                    }
                }
            })
            .build()
    }
}