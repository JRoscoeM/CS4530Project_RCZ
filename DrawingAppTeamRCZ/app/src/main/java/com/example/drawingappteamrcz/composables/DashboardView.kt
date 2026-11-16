package com.example.drawingappteamrcz.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.drawingappteamrcz.DrawingViewModel
import androidx.compose.foundation.clickable
import com.example.drawingappteamrcz.DrawingEntity

@Composable
fun DashboardView(myVM: DrawingViewModel,
                  onNewDrawing: () -> Unit,
                  onOpenDrawing: (DrawingEntity) -> Unit)
{
    val drawings by myVM.getDrawings().collectAsState(initial = emptyList())
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(Modifier.fillMaxSize()) {
            Text(
                text = "My drawings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
            )
            if (drawings.isEmpty())
            {
                EmptyDrawingsMessage(Modifier.weight(1f))
            } else
            {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 140.dp),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(drawings, key = { it.id }) { drawing ->
                        DrawingThumbnail(
                            drawing = drawing,
                            onClick = { onOpenDrawing(drawing) }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onNewDrawing,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) { Text("+", style = MaterialTheme.typography.headlineMedium) }
    }
}

@Composable
private fun EmptyDrawingsMessage(modifier: Modifier)
{
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No saved drawings yet", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            "Tap + to create your first drawing.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DrawingThumbnail(
    drawing: DrawingEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = drawing.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

//private fun BrushColor.toColor() = when (this)
//{
//    BrushColor.RED -> Color.Red
//    BrushColor.GREEN -> Color.Green
//    BrushColor.BLUE -> Color.Blue
//}
