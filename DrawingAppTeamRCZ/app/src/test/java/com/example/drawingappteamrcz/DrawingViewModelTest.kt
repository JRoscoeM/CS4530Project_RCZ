package com.example.drawingappteamrcz

import com.example.drawingappteamrcz.drawing.BrushType
import com.example.drawingappteamrcz.drawing.DrawingPoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class DrawingViewModelTest {
    @Test
    fun selectingBrushUpdatesTheStateAndClosesTheMenu() {
        val viewModel = DrawingViewModel()

        viewModel.openBrushMenu()
        viewModel.selectShape(BrushType.RECTANGLE)

        assertEquals(BrushType.RECTANGLE, viewModel.uiState.value.selectedShape)
        assertFalse(viewModel.uiState.value.showShapeMenu)
    }

    @Test
    fun completedStrokeKeepsTheBrushSettingsFromWhenItStarted() {
        val viewModel = DrawingViewModel()
        viewModel.setPenSize(14f)
        viewModel.selectShape(BrushType.CIRCLE)
        viewModel.startStroke(DrawingPoint(1f, 1f))
        viewModel.extendStroke(DrawingPoint(4f, 6f))
        viewModel.setPenSize(30f)
        viewModel.finishStroke()

        val state = viewModel.uiState.value
        assertEquals(emptyList<DrawingPoint>(), state.currentStroke)
        assertEquals(1, state.strokes.size)
        assertEquals(14f, state.strokes.single().width)
        assertEquals(BrushType.CIRCLE, state.strokes.single().type)
        assertEquals(
            listOf(DrawingPoint(1f, 1f), DrawingPoint(4f, 6f)),
            state.strokes.single().points
        )
    }
}
