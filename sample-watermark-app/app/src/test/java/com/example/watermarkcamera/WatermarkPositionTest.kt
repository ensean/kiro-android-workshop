package com.example.watermarkcamera

import com.example.watermarkcamera.watermark.*
import org.junit.Assert.*
import org.junit.Test

class WatermarkPositionTest {

    @Test
    fun `calculatePosition returns correct position for top-left preset`() {
        val result = WatermarkPosition.calculatePosition(
            PositionPreset.TOP_LEFT, Orientation.PORTRAIT, 1000, 800
        )
        assertEquals(20f, result.x, 0.1f)
        assertEquals(50f, result.y, 0.1f)
    }

    @Test
    fun `calculatePosition returns correct position for bottom-right preset`() {
        val result = WatermarkPosition.calculatePosition(
            PositionPreset.BOTTOM_RIGHT, Orientation.PORTRAIT, 1000, 800
        )
        assertEquals(780f, result.x, 0.1f)
        assertEquals(780f, result.y, 0.1f)
    }

    // 这个测试会失败 - 用于 Bugfix Spec 练习
    @Test
    fun `landscape orientation should not cause position overflow`() {
        val result = WatermarkPosition.calculatePosition(
            PositionPreset.BOTTOM_RIGHT, Orientation.LANDSCAPE, 1000, 800
        )
        // 横屏模式下位置不应该超出边界
        assertTrue("X position should be within bounds", result.x <= 1000)
    }

    @Test
    fun `relativeToAbsolute converts correctly`() {
        val result = WatermarkPosition.relativeToAbsolute(0.5f, 0.5f, 1000, 800)
        assertEquals(500f, result.x, 0.1f)
        assertEquals(400f, result.y, 0.1f)
    }

    @Test
    fun `absoluteToRelative converts correctly`() {
        val result = WatermarkPosition.absoluteToRelative(500f, 400f, 1000, 800)
        assertEquals(0.5f, result.x, 0.001f)
        assertEquals(0.5f, result.y, 0.001f)
    }

    @Test
    fun `isWithinBounds returns true for valid position`() {
        val result = WatermarkPosition.isWithinBounds(
            Position(100f, 100f), 50, 50, 1000, 800
        )
        assertTrue(result)
    }

    @Test
    fun `isWithinBounds returns false for overflow position`() {
        val result = WatermarkPosition.isWithinBounds(
            Position(980f, 100f), 50, 50, 1000, 800
        )
        assertFalse(result)
    }

    @Test
    fun `clampToBounds keeps position within bounds`() {
        val result = WatermarkPosition.clampToBounds(
            Position(980f, 100f), 50, 50, 1000, 800
        )
        assertEquals(950f, result.x, 0.1f)
        assertEquals(100f, result.y, 0.1f)
    }
}
