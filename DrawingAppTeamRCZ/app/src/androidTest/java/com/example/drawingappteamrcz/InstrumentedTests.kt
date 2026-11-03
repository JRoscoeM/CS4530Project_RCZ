package com.example.drawingappteamrcz

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.room3.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue

class DrawingViewUiTest
{
    @get:Rule
    val composeTestRule = createComposeRule()

//    @Test
//    fun choosingBrushShapeDismissesTheShapeMenu() {
//        val viewModel = DrawingViewModel()
//
//        composeTestRule.setContent {
//            DrawingRoute(viewModel = viewModel)
//        }
//
//        composeTestRule.onNodeWithText("Line").performClick()
//        composeTestRule.onNodeWithText("Circle").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Rectangle").assertIsDisplayed()
//
//        composeTestRule.onNodeWithText("Circle").performClick()
//
//        composeTestRule.onAllNodesWithText("Rectangle").assertCountEquals(0)
//        composeTestRule.onNodeWithText("Circle").assertIsDisplayed()
//    }
}

class DrawingDatabaseTest {
    private lateinit var database: DrawingDatabase
    private lateinit var dao: DrawingInterface

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        database = Room.inMemoryDatabaseBuilder(
            context,
            DrawingDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.drawingDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun updatingExistingDrawingDoesNotCreateCopy() = runBlocking {
        val drawingId = dao.insertDrawing(
            DrawingEntity(
                name = "Original",
                drawingData = "original strokes"
            )
        ).toInt()

        dao.updateDrawing(
            DrawingEntity(
                id = drawingId,
                name = "Edited",
                drawingData = "edited strokes"
            )
        )

        val drawings = dao.retrieveAllDrawings().first()

        assertEquals(1, drawings.size)
        assertEquals(drawingId, drawings.single().id)
        assertEquals("Edited", drawings.single().name)
        assertEquals("edited strokes", drawings.single().drawingData)
    }
}

