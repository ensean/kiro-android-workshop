package com.example.watermarkcamera.watermark

import android.graphics.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 水印渲染器 - 核心模块
 * 负责将水印渲染到图片上
 */
class WatermarkRenderer {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

    /**
     * 渲染水印到图片
     * 
     * @param source 原始图片
     * @param config 水印配置
     * @return 添加水印后的图片
     */
    fun render(source: Bitmap, config: WatermarkConfig): Bitmap {
        // 创建可变副本
        val result = source.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)

        // 计算水印位置
        val position = WatermarkPosition.calculatePosition(
            config.position,
            config.orientation,
            source.width,
            source.height
        )

        // 根据类型渲染水印
        when (config.type) {
            WatermarkType.TEXT -> renderTextWatermark(canvas, config, position)
            WatermarkType.IMAGE -> renderImageWatermark(canvas, config, position)
            WatermarkType.DATETIME -> renderDateTimeWatermark(canvas, config, position)
            WatermarkType.LOCATION -> renderLocationWatermark(canvas, config, position)
        }

        return result
    }

    private fun renderTextWatermark(
        canvas: Canvas,
        config: WatermarkConfig,
        position: Position
    ) {
        config.text?.let { text ->
            setupPaint(config)
            canvas.drawText(text, position.x, position.y, paint)
        }
    }

    private fun renderImageWatermark(
        canvas: Canvas,
        config: WatermarkConfig,
        position: Position
    ) {
        // 图片水印渲染逻辑
        // 实际实现需要加载水印图片
        config.watermarkImagePath?.let { path ->
            val watermarkBitmap = BitmapFactory.decodeFile(path)
            watermarkBitmap?.let { bitmap ->
                paint.alpha = (config.opacity * 255).toInt()
                
                val destRect = RectF(
                    position.x,
                    position.y,
                    position.x + (config.watermarkWidth ?: bitmap.width),
                    position.y + (config.watermarkHeight ?: bitmap.height)
                )
                
                canvas.drawBitmap(bitmap, null, destRect, paint)
                bitmap.recycle()
            }
        }
    }

    private fun renderDateTimeWatermark(
        canvas: Canvas,
        config: WatermarkConfig,
        position: Position
    ) {
        val dateText = dateFormat.format(Date())
        setupPaint(config)
        canvas.drawText(dateText, position.x, position.y, paint)
    }

    private fun renderLocationWatermark(
        canvas: Canvas,
        config: WatermarkConfig,
        position: Position
    ) {
        config.locationText?.let { text ->
            setupPaint(config)
            canvas.drawText(text, position.x, position.y, paint)
        }
    }

    private fun setupPaint(config: WatermarkConfig) {
        paint.apply {
            color = config.textColor
            textSize = config.fontSize
            alpha = (config.opacity * 255).toInt()
            typeface = Typeface.create(config.fontFamily, Typeface.NORMAL)
            setShadowLayer(2f, 1f, 1f, Color.BLACK)
        }
    }

    /**
     * 批量渲染水印
     */
    fun renderBatch(
        sources: List<Bitmap>,
        config: WatermarkConfig
    ): List<Bitmap> {
        return sources.map { render(it, config) }
    }
}
