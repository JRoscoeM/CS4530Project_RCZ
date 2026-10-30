package com.example.drawingappteamrcz.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.drawingappteamrcz.DrawingEntity
import com.example.drawingappteamrcz.DrawingViewModel
import com.example.drawingappteamrcz.drawing.BrushType
import com.example.drawingappteamrcz.drawing.DrawingPoint
import com.example.drawingappteamrcz.drawing.DrawingUiState
import com.example.drawingappteamrcz.encodeDrawing

import kotlin.math.abs

val all_colors = mapOf(
    "Red" to Color.Red,
    "Blue" to Color.Blue,
    "Green" to Color.Green,
    "Yellow" to Color.Yellow,
    "Black" to Color.Black,
    "White" to Color.White,
    "Gray" to Color.Gray,
    "Cyan" to Color.Cyan,
    "Magenta" to Color.Magenta
)

var startPosition: DrawingPoint? = null
var endPosition: DrawingPoint? = null

@Composable
fun DrawingRoute(viewModel: DrawingViewModel, onFinished: () -> Unit = {}, myNav: NavHostController)
{
    val uiState by viewModel.uiState.collectAsState()

    DrawingView(
        vm = viewModel,
        nav = myNav,
        uiState = uiState,
        onPenSizeChanged = viewModel::setPenSize,
        onBrushMenuRequested = viewModel::openBrushMenu,
        onBrushMenuDismissed = viewModel::dismissBrushMenu,
        onColorMenuRequested = viewModel::openColorMenu,
        onColorMenuDismissed = viewModel::dismissColorMenu,
        onShapeSelected = viewModel::selectShape,
        onColorSelected = viewModel::selectColor,
        onStrokeStarted = viewModel::startStroke,
        onStrokeExtended = viewModel::extendStroke,
        onStrokeFinished = viewModel::finishStroke,
        onFinished = onFinished
    )
}

@Composable
fun DrawingView(
    vm: DrawingViewModel,
    nav: NavHostController,
    uiState: DrawingUiState,
    onPenSizeChanged: (Float) -> Unit,
    onBrushMenuRequested: () -> Unit,
    onBrushMenuDismissed: () -> Unit,
    onColorMenuRequested: () -> Unit,
    onColorMenuDismissed: () -> Unit,
    onShapeSelected: (BrushType) -> Unit,
    onColorSelected: (Color) -> Unit,
    onStrokeStarted: (DrawingPoint) -> Unit,
    onStrokeExtended: (DrawingPoint) -> Unit,
    onStrokeFinished: () -> Unit,
    onFinished: () -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Brush size: ${uiState.penSize.toInt()}")
                Slider(
                    value = uiState.penSize,
                    onValueChange = onPenSizeChanged,
                    valueRange = 0f..100f,
                    steps = 99,
                    modifier = Modifier.fillMaxWidth()
                )
            }



            Box {
                Button(onClick = onBrushMenuRequested) {
                    Text(uiState.selectedShape.label)
                }

                DropdownMenu(
                    expanded = uiState.showShapeMenu,
                    onDismissRequest = onBrushMenuDismissed
                ) {
                    BrushType.entries.forEach { brush ->
                        DropdownMenuItem(
                            text = { Text(brush.label) },
                            onClick = { onShapeSelected(brush) }
                        )
                    }
                }
            }

            Box {
                Button(onClick = onColorMenuRequested) {
                    Text(
                        all_colors.entries.find { it.value == uiState.selectedColor }?.key
                            ?: "Select Color"
                    )
                }

                DropdownMenu(
                    expanded = uiState.showColorMenu,
                    onDismissRequest = onColorMenuDismissed
                ) {
                    all_colors.forEach { (name, color) ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = { onColorSelected(color) }
                        )
                    }
                }
            }
        }
        var textValue by remember { mutableStateOf("") }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Name this Drawing: ")
            TextField(
                value = textValue,
                onValueChange = { textValue = it },
                modifier = Modifier
                    .weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    val drawing = DrawingEntity(
                        name = textValue,
                        drawingData = encodeDrawing(uiState.strokes),

                        )
                    vm.addDrawing(
                        drawing.name,
                        drawing.drawingData
                    )

                }
            ) {
                Text("Save")
            }
            Box {
                Button(onClick = {
                    nav.navigate("dashboard")
                }) {
                    Text(text = "Back to Home")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .pointerInput(uiState.selectedShape) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            startPosition = offset.toDrawingPoint()
                            endPosition = startPosition
                            if (uiState.selectedShape == BrushType.DRAW) {
                                onStrokeStarted(startPosition!!)
                            }
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            endPosition = change.position.toDrawingPoint()
                            if (uiState.selectedShape == BrushType.DRAW) {
                                onStrokeExtended(endPosition!!)
                            }
                        },
                        onDragEnd = {
                            if (uiState.selectedShape == BrushType.DRAW) {
                                onStrokeFinished()
                            } else {
                                startPosition?.let { start ->
                                    endPosition?.let { end ->
                                        onStrokeStarted(start)
                                        onStrokeExtended(end)
                                        onStrokeFinished()
                                    }
                                }
                            }
                            startPosition = null
                            endPosition = null
                        },
                        onDragCancel = {
                            if (uiState.selectedShape == BrushType.DRAW) {
                                onStrokeFinished()
                            }
                            startPosition = null
                            endPosition = null
                        }
                    )
                }
        ) {
            uiState.strokes.forEach { stroke ->
                stroke.points.zipWithNext().forEach { (start, end) ->
                    if (stroke.type == BrushType.LINE || stroke.type == BrushType.DRAW)
                    {
                        clipRect {
                            drawLine(
                                color = stroke.color,
                                start = Offset(start.x, start.y),
                                end = Offset(end.x, end.y),
                                strokeWidth = stroke.width,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                    if (stroke.type == BrushType.CIRCLE)
                    {
                        clipRect {
                            drawCircle(
                                color = stroke.color,
                                center = Offset(start.x, start.y),
                                radius = Offset(end.x - start.x, end.y - start.y).getDistance(),
                                style = Stroke(width = stroke.width)
                            )
                        }
                    }
                    if (stroke.type == BrushType.RECTANGLE)
                    {
                        clipRect {
                            drawRect(
                                color = stroke.color,
                                topLeft = Offset(
                                    minOf(start.x, end.x),
                                    minOf(start.y, end.y)
                                ),
                                size = Size(
                                    abs(end.x - start.x),
                                    abs(end.y - start.y)
                                ),
                                style = Stroke(width = stroke.width)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Offset.toDrawingPoint() = DrawingPoint(x = x, y = y)
