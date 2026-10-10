package com.example.drawingappteamrcz.drawing

import androidx.compose.ui.graphics.Color

data class DrawingPoint(
    val x: Float,
    val y: Float
)

enum class BrushType(val label: String) {
    LINE("Line"),
    CIRCLE("Circle"),
    RECTANGLE("Rectangle")
}

data class Stroke(
    val points: List<DrawingPoint>,
    val width: Float,
    val type: BrushType,
    val color: Color
)

data class DrawingUiState(
    val penSize: Float = 10f,
    val showShapeMenu: Boolean = false,
    val showColorMenu: Boolean = false,
    val selectedShape: BrushType = BrushType.LINE,
    val selectedColor: Color = Color.Red,
    val strokes: List<Stroke> = emptyList(),
    val currentStroke: List<DrawingPoint> = emptyList()
)
