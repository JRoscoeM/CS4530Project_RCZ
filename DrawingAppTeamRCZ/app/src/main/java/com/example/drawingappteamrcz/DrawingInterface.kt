package com.example.drawingappteamrcz

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DrawingInterface
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrawing(drawing: DrawingEntity)

    @Query("SELECT * FROM drawings")
    fun retrieveAllDrawings(): Flow<List<DrawingEntity>>

    @Query("SELECT * FROM drawings WHERE name = :name")
    suspend fun retrieveDrawing(name: String): DrawingEntity
}