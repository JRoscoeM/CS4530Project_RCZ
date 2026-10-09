package com.example.drawingappteamrcz

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.drawingappteamrcz.composables.DrawingRoute
import org.junit.Rule
import org.junit.Test

class DrawingViewUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun choosingBrushShapeDismissesTheShapeMenu() {
        val viewModel = DrawingViewModel()

        composeTestRule.setContent {
            DrawingRoute(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Line").performClick()
        composeTestRule.onNodeWithText("Circle").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rectangle").assertIsDisplayed()

        composeTestRule.onNodeWithText("Circle").performClick()

        composeTestRule.onAllNodesWithText("Rectangle").assertCountEquals(0)
        composeTestRule.onNodeWithText("Circle").assertIsDisplayed()
    }
}
