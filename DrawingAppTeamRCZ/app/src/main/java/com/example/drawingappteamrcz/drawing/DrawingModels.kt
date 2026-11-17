package com.example.drawingappteamrcz.drawing

import androidx.compose.ui.graphics.Color
import android.graphics.Bitmap
import com.example.drawingappteamrcz.HTTPClient.LocalizedObjectAnnotation

data class DrawingPoint(
    val x: Float,
    val y: Float
)

enum class BrushType(val label: String)
{
    DRAW("Draw"),
    LINE("Line"),
    CIRCLE("Circle"),
    RECTANGLE("Rectangle")
}

//enum class BrushColor(val label: String)
//{
//    RED("Red"),
//    GREEN("Green"),
//    BLUE("Blue")
//}

data class Stroke(
    val points: List<DrawingPoint>,
    val width: Float,
    val type: BrushType,
    val color: Color = Color.Red
)

data class DrawingUiState(
    val drawingName: String = "",
    val penSize: Float = 10f,
    val showShapeMenu: Boolean = false,
    val showColorMenu: Boolean = false,
    val selectedShape: BrushType = BrushType.DRAW,
    val selectedColor: Color = Color.Red,
    val strokes: List<Stroke> = emptyList(),
    val currentStroke: List<DrawingPoint> = emptyList(),
    // Vision API (temporary plain-text display)
    val isDetecting: Boolean = false,
    val visionResult: String? = null,
    val importedImage: Bitmap? = null,
    val imageImportError: String? = null,
    val detectedObjects: List<LocalizedObjectAnnotation> = emptyList(),
)
