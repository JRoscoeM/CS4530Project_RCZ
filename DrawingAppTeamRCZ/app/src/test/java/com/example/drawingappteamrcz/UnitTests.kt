package com.example.drawingappteamrcz

import com.example.drawingappteamrcz.drawing.BrushType
import com.example.drawingappteamrcz.drawing.DrawingPoint
import com.example.drawingappteamrcz.drawing.Stroke
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Test

class DrawingModelTest {
    @Test
    fun strokeStoresItsDrawingParameters() {
        val points = listOf(DrawingPoint(12f, 8f), DrawingPoint(22f, 18f))

        val stroke = Stroke(points = points, width = 6f, type = BrushType.RECTANGLE)

        assertEquals(points, stroke.points)
        assertEquals(6f, stroke.width)
        assertEquals(BrushType.RECTANGLE, stroke.type)
    }

    @Test
    fun copyingStrokeForDragUpdateReplacesOnlyItsPoints() {
        val original = Stroke(
            points = listOf(DrawingPoint(1f, 1f)),
            width = 14f,
            type = BrushType.CIRCLE
        )
        val updatedPoints = listOf(DrawingPoint(1f, 1f), DrawingPoint(4f, 6f))

        val updated = original.copy(points = updatedPoints)

        assertNotSame(original, updated)
        assertEquals(updatedPoints, updated.points)
        assertEquals(14f, updated.width)
        assertEquals(BrushType.CIRCLE, updated.type)
        assertEquals(listOf(DrawingPoint(1f, 1f)), original.points)
    }

    @Test
    fun supportedBrushTypesMatchTheShapeMenu() {
        assertEquals(
            setOf(BrushType.LINE, BrushType.CIRCLE, BrushType.RECTANGLE),
            BrushType.values().toSet()
        )
    }
}
