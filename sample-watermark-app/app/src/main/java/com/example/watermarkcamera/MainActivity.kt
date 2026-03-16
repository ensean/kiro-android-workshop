package com.example.watermarkcamera

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.example.watermarkcamera.location.LocationHelper
import com.example.watermarkcamera.template.TemplateManager
import com.example.watermarkcamera.watermark.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var cameraPreview: PreviewView
    private lateinit var btnCapture: FloatingActionButton
    private lateinit var btnSettings: ImageButton
    private lateinit var btnTemplates: ImageButton
    private lateinit var watermarkPreview: android.widget.ImageView

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var templateManager: TemplateManager
    private lateinit var watermarkRenderer: WatermarkRenderer
    private lateinit var locationHelper: LocationHelper

    private var currentConfig: WatermarkConfig = WatermarkConfig(
        type = WatermarkType.DATETIME,
        position = PositionPreset.BOTTOM_RIGHT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initComponents()

        if (allPermissionsGranted()) {
            startCamera()
            locationHelper.startUpdates()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun initViews() {
        cameraPreview = findViewById(R.id.cameraPreview)
        btnCapture = findViewById(R.id.btnCapture)
        btnSettings = findViewById(R.id.btnSettings)
        btnTemplates = findViewById(R.id.btnTemplates)
        watermarkPreview = findViewById(R.id.watermarkPreview)

        btnCapture.setOnClickListener { takePhoto() }
        btnSettings.setOnClickListener { showSettingsDialog() }
        btnTemplates.setOnClickListener { showTemplatesDialog() }
        watermarkPreview.setOnClickListener { exitPreviewMode() }
    }

    private fun initComponents() {
        templateManager = TemplateManager(this)
        watermarkRenderer = WatermarkRenderer()
        locationHelper = LocationHelper(this)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(cameraPreview.surfaceProvider) }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e(TAG, "Camera binding failed", e)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            getExternalFilesDir(null),
            SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    applyWatermarkAndSave(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                    Toast.makeText(this@MainActivity, "拍照失败", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun applyWatermarkAndSave(photoFile: File) {
        // 拍照时立即抓取当前位置
        val location = locationHelper.getLastLocation()

        cameraExecutor.execute {
            try {
                val originalBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

                // 构建带位置信息的水印配置
                val config = if (location != null) {
                    val locationText = locationHelper.formatForWatermark(location)
                    currentConfig.copy(
                        type = WatermarkType.LOCATION,
                        locationText = locationText
                    )
                } else {
                    currentConfig
                }

                val watermarkedBitmap = watermarkRenderer.render(originalBitmap, config)

                val filename = SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA)
                    .format(System.currentTimeMillis()) + ".jpg"

                val saved = saveToGallery(watermarkedBitmap, filename, location)

                photoFile.delete()

                runOnUiThread {
                    if (saved) {
                        val msg = if (location != null) "照片已保存到相册（含GPS）" else "照片已保存到相册（无GPS信号）"
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        enterPreviewMode(watermarkedBitmap)
                    } else {
                        Toast.makeText(this, "保存到相册失败", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Watermark rendering failed", e)
                runOnUiThread {
                    Toast.makeText(this, "水印添加失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveToGallery(bitmap: Bitmap, filename: String, location: android.location.Location? = null): Boolean {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/WatermarkCamera")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: return false

            // 先写图片数据
            contentResolver.openOutputStream(uri)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            }

            // 写入 EXIF GPS 信息
            if (location != null) {
                contentResolver.openFileDescriptor(uri, "rw")?.use { pfd ->
                    val exif = ExifInterface(pfd.fileDescriptor)
                    exif.setLatLong(location.latitude, location.longitude)
                    if (location.hasAltitude()) {
                        exif.setAttribute(
                            ExifInterface.TAG_GPS_ALTITUDE,
                            locationHelper.toExifDMS(location.altitude)
                        )
                        exif.setAttribute(
                            ExifInterface.TAG_GPS_ALTITUDE_REF,
                            if (location.altitude >= 0) "0" else "1"
                        )
                    }
                    exif.saveAttributes()
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Save to gallery failed", e)
            false
        }
    }

    private fun enterPreviewMode(bitmap: Bitmap) {
        watermarkPreview.setImageBitmap(bitmap)
        watermarkPreview.visibility = android.view.View.VISIBLE
        watermarkPreview.bringToFront()
        btnCapture.visibility = android.view.View.GONE
    }

    private fun exitPreviewMode() {
        watermarkPreview.visibility = android.view.View.GONE
        watermarkPreview.setImageBitmap(null)
        btnCapture.visibility = android.view.View.VISIBLE
    }

    private fun showSettingsDialog() {
        // TODO: 实现水印设置对话框
        Toast.makeText(this, "水印设置 - Workshop 练习内容", Toast.LENGTH_SHORT).show()
    }

    private fun showTemplatesDialog() {
        // TODO: 实现模板管理对话框
        val templates = templateManager.getAll()
        Toast.makeText(this, "已有 ${templates.size} 个模板", Toast.LENGTH_SHORT).show()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
                locationHelper.startUpdates()
            } else {
                Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (watermarkPreview.visibility == android.view.View.VISIBLE) {
            exitPreviewMode()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        locationHelper.stopUpdates()
    }

    companion object {
        private const val TAG = "WatermarkCamera"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = buildList {
            add(Manifest.permission.CAMERA)
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}
