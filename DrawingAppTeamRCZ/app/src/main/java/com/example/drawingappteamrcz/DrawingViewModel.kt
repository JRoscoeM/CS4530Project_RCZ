package com.example.drawingappteamrcz

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.drawingappteamrcz.drawing.BrushType
import com.example.drawingappteamrcz.drawing.DrawingPoint
import com.example.drawingappteamrcz.drawing.DrawingUiState
import com.example.drawingappteamrcz.drawing.Stroke
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// TODO type of Drawing
@Serializable
data class Drawing(val name: String, val drawingData: String)

//TODO this may be useful later
//object APIClient {
//    val client = HttpClient(Android) {
//        install(ContentNegotiation) {
//            json(Json{ ignoreUnknownKeys = true })
//        }
//    }
//}

object DrawingViewModelProvider
{
    val Factory = viewModelFactory {
        initializer {
            DrawingViewModel(
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as DrawingApplication).repository
            )
        }
    }
}

class DrawingViewModel(repository: Repository) : ViewModel()
{

    //    private val client = APIClient.client
    private val dao = repository

    // TODO is the above line redundant?
    private val _uiState = MutableStateFlow(DrawingUiState())
    val uiState: StateFlow<DrawingUiState> = _uiState.asStateFlow()
    val drawingsReadOnly: Flow<List<DrawingEntity>> = dao.getAllDrawings().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(), // TODO give WhileSubscribed() a param?
        emptyList()
    )
    // TODO make me private? getDrawings() is an accessor for this

    // ----------- Start Data Functions ------------
    fun getDrawings(): Flow<List<DrawingEntity>>
    {
        return drawingsReadOnly
    }

    suspend fun getDrawing(name: String)
    {
        try
        {
            dao.getDrawing(name)
        } catch (e: Exception)
        {
            Log.e("In Drawing VM", "Error getting drawing", e)
        }
    }
    //TODO: purpose is different than A4, maybe no try/catch is needed.

    // ----------- End Data Functions ------------
    fun setPenSize(size: Float)
    {
        _uiState.update { state ->
            state.copy(penSize = size.coerceIn(MIN_PEN_SIZE, MAX_PEN_SIZE))
        }
    }

    fun openBrushMenu()
    {
        _uiState.update { it.copy(showShapeMenu = true) }
    }

    fun dismissBrushMenu()
    {
        _uiState.update { it.copy(showShapeMenu = false) }
    }

    fun openColorMenu()
    {
        _uiState.update { it.copy(showColorMenu = true) }
    }

    fun dismissColorMenu()
    {
        _uiState.update { it.copy(showColorMenu = false) }
    }

    fun selectShape(shape: BrushType)
    {
        _uiState.update {
            it.copy(
                selectedShape = shape,
                showShapeMenu = false
            )
        }
    }

    fun selectColor(setColor: Color)
    {
        _uiState.update {
            it.copy(
                selectedColor = setColor,
                showColorMenu = false
            )
        }
    }

    fun startStroke(point: DrawingPoint)
    {
        _uiState.update { state ->
            val currentStroke = listOf(point)

            state.copy(
                currentStroke = currentStroke,
                strokes = state.strokes + Stroke(
                    points = currentStroke,
                    width = state.penSize,
                    type = state.selectedShape,
                    color = state.selectedColor
                )
            )
        }
    }

    fun extendStroke(point: DrawingPoint)
    {
        _uiState.update { state ->
            if (state.currentStroke.isEmpty() || state.strokes.isEmpty())
            {
                return@update state
            }

            val currentStroke = state.currentStroke + point

            state.copy(
                currentStroke = currentStroke,
                strokes = state.strokes.dropLast(1) + state.strokes.last()
                    .copy(points = currentStroke)
            )
        }
    }

    fun finishStroke()
    {
        _uiState.update { it.copy(currentStroke = emptyList()) }
    }

    private companion object
    {
        const val MIN_PEN_SIZE = 0f
        const val MAX_PEN_SIZE = 100f
    }
}
