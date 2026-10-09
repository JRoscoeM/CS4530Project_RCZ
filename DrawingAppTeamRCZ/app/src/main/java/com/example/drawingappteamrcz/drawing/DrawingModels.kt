package com.example.drawingappteamrcz.drawing

data class DrawingPoint(
    val x: Float,
    val y: Float
)

enum class BrushType(val label: String) {
    LINE("Line"),
    CIRCLE("Circle"),
    RECTANGLE("Rectangle")
}

enum class BrushColor(val label: String){
    RED("Red"),
    GREEN("Green"),
    BLUE("Blue")
}

data class Stroke(
    val points: List<DrawingPoint>,
    val width: Float,
    val type: BrushType,
    val color: BrushColor
)

data class DrawingUiState(
    val penSize: Float = 10f,
    val showShapeMenu: Boolean = false,
    val showColorMenu: Boolean = false,
    val selectedShape: BrushType = BrushType.LINE,
    val selectedColor: BrushColor = BrushColor.RED,
    val strokes: List<Stroke> = emptyList(),
    val currentStroke: List<DrawingPoint> = emptyList()
)
