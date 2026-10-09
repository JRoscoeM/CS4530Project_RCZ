package com.example.drawingappteamrcz.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashView(onFinished: () -> Unit)
{
    LaunchedEffect(Unit) {
        delay(1_500)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PaletteMark()
            Text(
                text = "Drawing App",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Draw stuff",
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                fontSize = 16.sp
            )
        }
    }

}

@Composable
private fun PaletteMark() {
    val splashBackground = MaterialTheme.colorScheme.primaryContainer

    Canvas(modifier = Modifier.size(112.dp)) {
        val radius = size.minDimension / 2f
        val center = Offset(radius, radius)

        drawCircle(Color.White.copy(alpha = 0.9f), radius, center)
        drawCircle(
            color = Color(0xFF5133C4),
            radius = radius,
            center = center,
            style = Stroke(width = 5.dp.toPx())
        )
        drawCircle(Color(0xFFFF766A), radius * 0.13f, Offset(radius * 0.62f, radius * 0.48f))
        drawCircle(Color(0xFFFFC857), radius * 0.13f, Offset(radius * 1.18f, radius * 0.57f))
        drawCircle(Color(0xFF36C5A8), radius * 0.13f, Offset(radius * 0.78f, radius * 1.2f))
        drawCircle(
            splashBackground,
            radius * 0.18f,
            Offset(radius * 1.25f, radius * 1.25f)
        )
    }
}
