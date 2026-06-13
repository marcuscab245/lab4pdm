package com.example.lab4pdm

import android.app.Application
import androidx.room.Room
import com.example.lab4pdm.model.AppDatabase

class InitDatabase : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "TaskDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}