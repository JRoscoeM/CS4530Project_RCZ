package com.example.drawingappteamrcz

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

    //TODO Should this be suspend?
    fun addDrawing(drawing: DrawingEntity)
    {
        scope.launch {
//            val drawingObj = DrawingEntity(drawing.name, drawing.drawingData)
            // TDOO: The commented line seems redundant, is it?
            dao.insertDrawing(drawing)
        }
    }

}