package com.example.watermarkcamera.template

import com.example.watermarkcamera.watermark.*

/** 默认水印模板 */
object DefaultTemplates {
    val templates = listOf(
        "简约时间戳" to WatermarkConfig(
            type = WatermarkType.DATETIME,
            position = PositionPreset.BOTTOM_RIGHT,
            fontSize = 24f, opacity = 0.8f
        ),
        "经典水印" to WatermarkConfig(
            type = WatermarkType.TEXT,
            position = PositionPreset.BOTTOM_LEFT,
            text = "© 水印相机", fontSize = 20f, opacity = 0.6f
        ),
        "位置标记" to WatermarkConfig(
            type = WatermarkType.LOCATION,
            position = PositionPreset.BOTTOM_LEFT,
            fontSize = 18f, textColor = 0xFFFFCC00.toInt(), opacity = 0.9f
        )
    )
}
