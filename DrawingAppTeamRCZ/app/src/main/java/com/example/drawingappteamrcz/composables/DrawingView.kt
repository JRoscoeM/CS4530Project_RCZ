package com.example.drawingappteamrcz.composables

import android.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.room.util.copy

@Composable
fun DrawingView(onFinished: () -> Unit)
{
    // Variables for Pen Settings
    var penSize by remember { mutableFloatStateOf(10f) }
    var showShapeMenu by remember { mutableStateOf(false) }
    var selectedShape by remember { mutableStateOf(BrushType.LINE) }

    // Variables for Canvas
    var strokes by remember { mutableStateOf(listOf<Stroke>()) }
    var currentStroke by remember { mutableStateOf(listOf< Offset>())}

    // Row 1 - Modifiers, Row 2 - Canvas
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        // 3 Columns for each modifier in Row 1. 2 implimented so far
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Slider(
                value = penSize,
                onValueChange = { penSize = it },
                valueRange = 0f..100f,
                steps = 99,
                modifier = Modifier.weight(1f)
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(onClick = { showShapeMenu = true }) {
                Text("Line")
            }

            DropdownMenu(
                expanded = showShapeMenu,
                onDismissRequest = { showShapeMenu = false }
            ) {
                listOf(
                    "Line" to BrushType.LINE,
                    "Circle" to BrushType.CIRCLE,
                    "Rectangle" to BrushType.RECTANGLE
                ).forEach { (name, type) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedShape = type
                            showShapeMenu = false
                        }
                    )
                }
            }
        }

    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentStroke = listOf(offset)

                            strokes = strokes + Stroke(
                                points = currentStroke,
                                width = penSize,
                                type = selectedShape
                            )
                        },

                        onDrag = { change, _ ->
                            change.consume()

                            currentStroke = currentStroke + change.position

                            // Update the most recent stroke while preserving
                            // its original width, color, and brush shape
                            strokes = strokes.dropLast(1) + strokes.last().copy(
                                points = currentStroke
                            )
                        },

                        onDragEnd = {
                            currentStroke = emptyList()
                        }
                    )
                }
        )
        {
            strokes.forEach { stroke ->
                for (i in 0 until stroke.points.size - 1) {
                    drawLine(
                        color = Color.Red,
                        start = stroke.points[i],
                        end = stroke.points[i + 1],
                        strokeWidth = stroke.width
                    )
                }
            }
        }
    }
}

enum class BrushType {
    LINE, CIRCLE, RECTANGLE
}

data class Stroke(
    val points: List<Offset>,
    val width: Float,
    val type: BrushType
)