package com.example.drawingappteamrcz

import androidx.lifecycle.ViewModel
import com.example.drawingappteamrcz.drawing.BrushType
import com.example.drawingappteamrcz.drawing.DrawingPoint
import com.example.drawingappteamrcz.drawing.DrawingUiState
import com.example.drawingappteamrcz.drawing.Stroke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DrawingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DrawingUiState())
    val uiState: StateFlow<DrawingUiState> = _uiState.asStateFlow()

    fun setPenSize(size: Float) {
        _uiState.update { state ->
            state.copy(penSize = size.coerceIn(MIN_PEN_SIZE, MAX_PEN_SIZE))
        }
    }

    fun openBrushMenu() {
        _uiState.update { it.copy(showShapeMenu = true) }
    }

    fun dismissBrushMenu() {
        _uiState.update { it.copy(showShapeMenu = false) }
    }

    fun selectShape(shape: BrushType) {
        _uiState.update {
            it.copy(
                selectedShape = shape,
                showShapeMenu = false
            )
        }
    }

    fun startStroke(point: DrawingPoint) {
        _uiState.update { state ->
            val currentStroke = listOf(point)

            state.copy(
                currentStroke = currentStroke,
                strokes = state.strokes + Stroke(
                    points = currentStroke,
                    width = state.penSize,
                    type = state.selectedShape
                )
            )
        }
    }

    fun extendStroke(point: DrawingPoint) {
        _uiState.update { state ->
            if (state.currentStroke.isEmpty() || state.strokes.isEmpty()) {
                return@update state
            }

            val currentStroke = state.currentStroke + point

            state.copy(
                currentStroke = currentStroke,
                strokes = state.strokes.dropLast(1) + state.strokes.last().copy(points = currentStroke)
            )
        }
    }

    fun finishStroke() {
        _uiState.update { it.copy(currentStroke = emptyList()) }
    }

    private companion object {
        const val MIN_PEN_SIZE = 0f
        const val MAX_PEN_SIZE = 100f
    }
}
