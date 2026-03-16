package com.example.watermarkcamera.watermark

/**
 * 水印位置计算模块
 * 处理不同方向和位置的坐标计算
 */
object WatermarkPosition {
    
    private const val PADDING = 20f
    private const val DEFAULT_WATERMARK_WIDTH = 200f

    /**
     * 计算水印的绝对位置
     * 
     * BUG: 横屏模式下位置计算有偏移问题
     * 这是 Workshop 中 Bugfix Spec 练习的目标
     * 
     * @param preset 预设位置
     * @param orientation 设备方向
     * @param imageWidth 图片宽度
     * @param imageHeight 图片高度
     * @return 计算后的位置坐标
     */
    fun calculatePosition(
        preset: PositionPreset,
        orientation: Orientation,
        imageWidth: Int,
        imageHeight: Int
    ): Position {
        var x: Float
        var y: Float

        // 基础位置计算
        when (preset) {
            PositionPreset.TOP_LEFT -> {
                x = PADDING
                y = PADDING + 30f
            }
            PositionPreset.TOP_RIGHT -> {
                x = imageWidth - PADDING - DEFAULT_WATERMARK_WIDTH
                y = PADDING + 30f
            }
            PositionPreset.BOTTOM_LEFT -> {
                x = PADDING
                y = imageHeight - PADDING
            }
            PositionPreset.BOTTOM_RIGHT -> {
                x = imageWidth - PADDING - DEFAULT_WATERMARK_WIDTH
                y = imageHeight - PADDING
            }
            PositionPreset.CENTER -> {
                x = imageWidth / 2f - 100f
                y = imageHeight / 2f
            }
            PositionPreset.CUSTOM -> {
                x = PADDING
                y = imageHeight - PADDING
            }
        }

        // 横屏模式调整 - 这里有 bug！
        // 问题：没有正确处理坐标系旋转
        if (orientation == Orientation.LANDSCAPE) {
            // BUG: 这个偏移量计算不正确，会导致水印超出边界
            x += 50f  // 错误的偏移
        }

        return Position(x, y)
    }

    /**
     * 将相对位置转换为绝对位置
     */
    fun relativeToAbsolute(
        relativeX: Float,
        relativeY: Float,
        imageWidth: Int,
        imageHeight: Int
    ): Position {
        return Position(
            x = relativeX * imageWidth,
            y = relativeY * imageHeight
        )
    }

    /**
     * 将绝对位置转换为相对位置
     */
    fun absoluteToRelative(
        absoluteX: Float,
        absoluteY: Float,
        imageWidth: Int,
        imageHeight: Int
    ): Position {
        return Position(
            x = absoluteX / imageWidth,
            y = absoluteY / imageHeight
        )
    }

    /**
     * 检查位置是否在图片边界内
     */
    fun isWithinBounds(
        position: Position,
        watermarkWidth: Int,
        watermarkHeight: Int,
        imageWidth: Int,
        imageHeight: Int
    ): Boolean {
        return position.x >= 0 &&
               position.y >= 0 &&
               position.x + watermarkWidth <= imageWidth &&
               position.y + watermarkHeight <= imageHeight
    }

    /**
     * 调整位置使其在边界内
     */
    fun clampToBounds(
        position: Position,
        watermarkWidth: Int,
        watermarkHeight: Int,
        imageWidth: Int,
        imageHeight: Int
    ): Position {
        return Position(
            x = position.x.coerceIn(0f, (imageWidth - watermarkWidth).toFloat()),
            y = position.y.coerceIn(0f, (imageHeight - watermarkHeight).toFloat())
        )
    }
}
