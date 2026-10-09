package com.example.drawingappteamrcz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.drawingappteamrcz.DrawingViewModel
import com.example.drawingappteamrcz.composables.DashboardView
import com.example.drawingappteamrcz.composables.DrawingView
import com.example.drawingappteamrcz.composables.SplashView
import androidx.compose.ui.Modifier
import com.example.drawingappteamrcz.composables.DrawingRoute

@Composable
fun DrawingAppNav(
    myVM: DrawingViewModel, myNavController: NavHostController, startDestination: String,
    modifier: Modifier = Modifier)
{
    NavHost(myNavController, startDestination, modifier = modifier) {
        composable("dashboard") { DashboardView(myVM) }
        composable("drawing") { DrawingRoute(viewModel = myVM, onFinished = {}) }
        composable("splash") {
            SplashView {
                myNavController.navigate("dashboard") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

}

