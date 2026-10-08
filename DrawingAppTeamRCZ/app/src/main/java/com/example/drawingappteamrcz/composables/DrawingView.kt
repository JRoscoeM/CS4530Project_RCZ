package com.example.drawingappteamrcz.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun DrawingView(onFinished: () -> Unit)
{
    // Variables for Pen Settings
    var PenSize by remember { mutableFloatStateOf(0f) }
    var showShapeMenu by remember { mutableStateOf(false) }
    var selectedShape by remember { mutableStateOf(StrokeCap.Round) }

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
                value = PenSize,
                onValueChange = { PenSize = it },
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
                Text(selectedShape)
            }

            DropdownMenu(
                expanded = showShapeMenu,
                onDismissRequest = { showShapeMenu = false }
            ) {
                listOf(
                    "Round" to StrokeCap.Round,
                    "Square" to StrokeCap.Square
                ).forEach { (name, cap) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedShape = cap
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
        )
        {

        }
    }
}