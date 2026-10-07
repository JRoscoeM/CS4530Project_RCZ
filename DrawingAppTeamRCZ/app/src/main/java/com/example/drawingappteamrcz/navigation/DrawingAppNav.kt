package com.example.drawingappteamrcz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.drawingappteamrcz.DrawingViewModel
import com.example.drawingappteamrcz.composables.DashboardView
import com.example.drawingappteamrcz.composables.DrawingView
import com.example.drawingappteamrcz.composables.SplashView

@Composable
fun DrawingAppNav(
    myVM: DrawingViewModel, myNavController: NavHostController, startDestination: String
)
{
    NavHost(myNavController, startDestination) {
        composable("dashboard") { DashboardView(myVM) }
        composable("drawing") { DrawingView() }
        composable("splash") { SplashView() }
    }

}
