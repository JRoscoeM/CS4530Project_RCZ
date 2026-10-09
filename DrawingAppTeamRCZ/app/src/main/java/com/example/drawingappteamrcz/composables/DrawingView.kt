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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.drawingappteamrcz.DrawingViewModel
import com.example.drawingappteamrcz.drawing.BrushColor
import com.example.drawingappteamrcz.drawing.BrushType
import com.example.drawingappteamrcz.drawing.DrawingPoint
import com.example.drawingappteamrcz.drawing.DrawingUiState

@Composable
fun DrawingRoute(viewModel: DrawingViewModel, onFinished: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()

    DrawingView(
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
    uiState: DrawingUiState,
    onPenSizeChanged: (Float) -> Unit,
    onBrushMenuRequested: () -> Unit,
    onBrushMenuDismissed: () -> Unit,
    onColorMenuRequested: () -> Unit,
    onColorMenuDismissed: () -> Unit,
    onShapeSelected: (BrushType) -> Unit,
    onColorSelected: (BrushColor) -> Unit,
    onStrokeStarted: (DrawingPoint) -> Unit,
    onStrokeExtended: (DrawingPoint) -> Unit,
    onStrokeFinished: () -> Unit,
    onFinished: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {
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
                    Text(uiState.selectedColor.label)
                }

                DropdownMenu(
                    expanded = uiState.showColorMenu,
                    onDismissRequest = onColorMenuDismissed
                ) {
                    BrushColor.entries.forEach { color ->
                        DropdownMenuItem(
                            text = { Text(color.label) },
                            onClick = { onColorSelected(color) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            onStrokeStarted(offset.toDrawingPoint())
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            onStrokeExtended(change.position.toDrawingPoint())
                        },
                        onDragEnd = onStrokeFinished,
                        onDragCancel = onStrokeFinished
                    )
                }
        ) {
            uiState.strokes.forEach { stroke ->
                stroke.points.zipWithNext().forEach { (start, end) ->
                    if(stroke.color == BrushColor.GREEN) {
                        drawLine(
                            color = Color.Green,
                            start = Offset(start.x, start.y),
                            end = Offset(end.x, end.y),
                            strokeWidth = stroke.width
                        )
                    }
                    if(stroke.color == BrushColor.RED) {
                        drawLine(
                            color = Color.Red,
                            start = Offset(start.x, start.y),
                            end = Offset(end.x, end.y),
                            strokeWidth = stroke.width
                        )
                    }
                    if(stroke.color == BrushColor.BLUE) {
                        drawLine(
                            color = Color.Blue,
                            start = Offset(start.x, start.y),
                            end = Offset(end.x, end.y),
                            strokeWidth = stroke.width
                        )
                    }
                }
            }
        }
    }
}

private fun Offset.toDrawingPoint() = DrawingPoint(x = x, y = y)
