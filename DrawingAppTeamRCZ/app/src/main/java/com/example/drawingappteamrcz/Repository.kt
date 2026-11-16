package com.example.drawingappteamrcz

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class Repository(
    val scope: CoroutineScope,
    private val dao: DrawingInterface
)
{
    // DELETEME: Note that functions returning a flow should not be suspend functions
    fun getAllDrawings(): Flow<List<DrawingEntity>>
    {
        return dao.retrieveAllDrawings()
    }

    suspend fun getDrawing(name: String): DrawingEntity
    {
        return dao.retrieveDrawing(name)
    }

    suspend fun addDrawing(drawing: DrawingEntity): Long {
        return dao.insertDrawing(drawing)
    }

    suspend fun updateDrawing(drawing: DrawingEntity) {
        dao.updateDrawing(drawing)
    }

}