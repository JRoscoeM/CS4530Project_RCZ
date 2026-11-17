package com.example.drawingappteamrcz.composables

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke as StrokeStyle
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.drawingappteamrcz.decodeDrawing
import com.example.drawingappteamrcz.drawing.BrushType
import com.example.drawingappteamrcz.drawing.Stroke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.drawingappteamrcz.DrawingViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

private sealed interface DrawingPreviewState {
    data object Empty : DrawingPreviewState
    data class Loaded(val strokes: List<Stroke>) : DrawingPreviewState
    data object Error : DrawingPreviewState
}

@Composable
fun AIView(
    navController: NavHostController,
    drawingUri: Uri?,
    viewModel: DrawingViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val importedImage = uiState.importedImage
    val previewState by produceState<DrawingPreviewState>(
        initialValue = DrawingPreviewState.Empty,
        drawingUri
    ) {
        value = drawingUri?.let { uri ->
            withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(uri)
                        ?.bufferedReader()
                        ?.use { reader -> decodeDrawing(reader.readText()) }
                        ?.let(DrawingPreviewState::Loaded)
                        ?: DrawingPreviewState.Error
                }.getOrElse { DrawingPreviewState.Error }
            }
        } ?: DrawingPreviewState.Empty
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Button(onClick = { navController.navigate("dashboard") }) {
            Text(text = "Home")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            when {
                importedImage != null -> {
                    Image(
                        bitmap = importedImage.asImageBitmap(),
                        contentDescription = "Imported gallery image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                else -> {
                    when (val state = previewState) {
                        is DrawingPreviewState.Loaded -> {
                            DrawingPreview(strokes = state.strokes)
                        }

                        DrawingPreviewState.Empty -> {
                            Text(
                                text = "No image selected",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DrawingPreviewState.Error -> {
                            Text(
                                text = "Unable to load drawing",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawingPreview(strokes: List<Stroke>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        strokes.forEach { stroke ->
            stroke.points.zipWithNext().forEach { (start, end) ->
                val startOffset = Offset(start.x, start.y)
                val endOffset = Offset(end.x, end.y)

                clipRect {
                    when (stroke.type) {
                        BrushType.LINE, BrushType.DRAW -> drawLine(
                            color = stroke.color,
                            start = startOffset,
                            end = endOffset,
                            strokeWidth = stroke.width,
                            cap = StrokeCap.Round
                        )

                        BrushType.CIRCLE -> drawCircle(
                            color = stroke.color,
                            center = startOffset,
                            radius = (endOffset - startOffset).getDistance(),
                            style = StrokeStyle(width = stroke.width)
                        )

                        BrushType.RECTANGLE -> drawRect(
                            color = stroke.color,
                            topLeft = Offset(
                                minOf(start.x, end.x),
                                minOf(start.y, end.y)
                            ),
                            size = Size(
                                kotlin.math.abs(end.x - start.x),
                                kotlin.math.abs(end.y - start.y)
                            ),
                            style = StrokeStyle(width = stroke.width)
                        )
                    }
                }
            }
        }
    }
}
