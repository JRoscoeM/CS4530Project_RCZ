package com.example.drawingappteamrcz.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.drawingappteamrcz.DrawingViewModel
import com.example.drawingappteamrcz.composables.DashboardView
import com.example.drawingappteamrcz.composables.SplashView
import androidx.compose.ui.Modifier
import com.example.drawingappteamrcz.composables.AIView
import com.example.drawingappteamrcz.composables.AI_IMPORTED_DRAWING_URI_KEY
import com.example.drawingappteamrcz.composables.DrawingRoute

@Composable
fun DrawingAppNav(
    myVM: DrawingViewModel, myNavController: NavHostController, startDestination: String,
    modifier: Modifier = Modifier
)
{
    NavHost(myNavController, startDestination, modifier = modifier) {
        composable("dashboard") {
            DashboardView(
                myVM = myVM,
                onNewDrawing = {
                    myVM.startNewDrawing()
                    myNavController.navigate("drawing")
                },
                onOpenDrawing = { drawing ->
                    myVM.loadDrawing(drawing)
                    myNavController.navigate("drawing")
                }
            )
        }
        composable("drawing") {
            DrawingRoute(
                viewModel = myVM,
                onFinished = {
                    myNavController.popBackStack()
                },
                myNavController
            )
        }
        composable("ai") {
            val drawingUri = myNavController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>(AI_IMPORTED_DRAWING_URI_KEY)
                ?.let(Uri::parse)

            AIView(
                navController = myNavController,
                drawingUri = drawingUri
            )
        }
        composable("splash") {
            SplashView {
                myNavController.navigate("dashboard") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

}

