package com.example.drawingappteamrcz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.drawingappteamrcz.navigation.DrawingAppNav
import com.example.drawingappteamrcz.ui.theme.DrawingAppTeamRCZTheme

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrawingAppTeamRCZTheme {
                val myVM: DrawingViewModel = viewModel()
                val myNavController = rememberNavController()
                DrawingAppNav(myVM, myNavController, "drawing")
                DrawingApp(modifier = Modifier.fillMaxSize())
            }
            }
        }
    }

@Composable
fun DrawingApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val drawingViewModel: DrawingViewModel = viewModel()

    DrawingAppNav(drawingViewModel, navController, "splash", modifier)
}

@Preview(showBackground = true)
@Composable
fun DrawingAppPreview()
{
    DrawingAppTeamRCZTheme {
        DrawingApp()
    }
}