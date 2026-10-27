package com.example.drawingappteamrcz

import android.app.Application
import androidx.room3.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.getValue

class DrawingApplication : Application()
{
    val scope = CoroutineScope(SupervisorJob())
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            DrawingDatabase::class.java,
            "drawing_database"
        ).build()
    }
    val repository by lazy {
        Repository(scope, database.drawingDao())
    }
}