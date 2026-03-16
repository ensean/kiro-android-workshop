package com.example.watermarkcamera.watermark

/**
 * 水印类型
 */
enum class WatermarkType {
    TEXT,       // 文字水印
    IMAGE,      // 图片水印
    DATETIME,   // 时间戳水印
    LOCATION    // 位置水印
}

/**
 * 预设位置
 */
enum class PositionPreset {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    CENTER,
    CUSTOM
}

/**
 * 设备方向
 */
enum class Orientation {
    PORTRAIT,   // 竖屏
    LANDSCAPE   // 横屏
}

/**
 * 坐标位置
 */
data class Position(
    val x: Float,
    val y: Float
)

/**
 * 水印配置
 */
data class WatermarkConfig(
    val type: WatermarkType,
    val position: PositionPreset,
    val orientation: Orientation = Orientation.PORTRAIT,
    
    // 文字水印配置
    val text: String? = null,
    val fontSize: Float = 24f,
    val fontFamily: String = "sans-serif",
    val textColor: Int = 0xFFFFFFFF.toInt(),
    
    // 图片水印配置
    val watermarkImagePath: String? = null,
    val watermarkWidth: Int? = null,
    val watermarkHeight: Int? = null,
    
    // 位置水印配置
    val locationText: String? = null,
    
    // 通用配置
    val opacity: Float = 1.0f,
    val rotation: Float = 0f
)

/**
 * 水印模板
 */
data class WatermarkTemplate(
    val id: String,
    val name: String,
    val config: WatermarkConfig,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
