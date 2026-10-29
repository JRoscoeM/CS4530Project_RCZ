package com.example.drawingappteamrcz

import androidx.room3.Database
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.RoomDatabase

@Database(entities = [DrawingEntity::class], version = 1, exportSchema = false)
abstract class DrawingDatabase : RoomDatabase()
{
    abstract fun drawingDao(): DrawingInterface
}

// TODO: drawingData Type is TBD
@Entity(tableName = "drawings")
data class DrawingEntity(
    val name: String,
    val drawingData: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)