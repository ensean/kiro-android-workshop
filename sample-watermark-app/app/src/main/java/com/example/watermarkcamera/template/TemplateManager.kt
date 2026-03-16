package com.example.watermarkcamera.template

import android.content.Context
import android.content.SharedPreferences
import com.example.watermarkcamera.watermark.WatermarkConfig
import com.example.watermarkcamera.watermark.WatermarkTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 水印模板管理器
 * 负责模板的 CRUD 操作和本地存储
 */
class TemplateManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "watermark_templates"
        private const val KEY_TEMPLATES = "templates"
        private const val MAX_TEMPLATES = 20
    }

    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val templates = mutableMapOf<String, WatermarkTemplate>()

    init {
        loadFromStorage()
    }

    /**
     * 获取所有模板
     */
    fun getAll(): List<WatermarkTemplate> {
        return templates.values
            .sortedByDescending { it.updatedAt }
    }

    /**
     * 根据 ID 获取模板
     */
    fun getById(id: String): WatermarkTemplate? {
        return templates[id]
    }

    /**
     * 创建新模板
     */
    fun create(name: String, config: WatermarkConfig): WatermarkTemplate {
        if (templates.size >= MAX_TEMPLATES) {
            throw IllegalStateException("模板数量已达上限 ($MAX_TEMPLATES)")
        }

        if (isNameExists(name)) {
            throw IllegalArgumentException("模板名称已存在: $name")
        }

        val template = WatermarkTemplate(
            id = generateId(),
            name = name,
            config = config,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        templates[template.id] = template
        saveToStorage()

        return template
    }

    /**
     * 更新模板
     */
    fun update(
        id: String,
        name: String? = null,
        config: WatermarkConfig? = null
    ): WatermarkTemplate {
        val existing = templates[id]
            ?: throw IllegalArgumentException("模板不存在: $id")

        name?.let {
            if (isNameExists(it, excludeId = id)) {
                throw IllegalArgumentException("模板名称已存在: $it")
            }
        }

        val updated = existing.copy(
            name = name ?: existing.name,
            config = config ?: existing.config,
            updatedAt = System.currentTimeMillis()
        )

        templates[id] = updated
        saveToStorage()

        return updated
    }

    /**
     * 删除模板
     */
    fun delete(id: String): Boolean {
        val removed = templates.remove(id) != null
        if (removed) {
            saveToStorage()
        }
        return removed
    }

    /**
     * 检查模板名称是否已存在
     */
    fun isNameExists(name: String, excludeId: String? = null): Boolean {
        return templates.values.any { 
            it.name == name && it.id != excludeId 
        }
    }

    /**
     * 获取模板数量
     */
    fun getCount(): Int = templates.size

    /**
     * 是否可以创建新模板
     */
    fun canCreate(): Boolean = templates.size < MAX_TEMPLATES

    /**
     * 清空所有模板
     */
    fun clearAll() {
        templates.clear()
        saveToStorage()
    }

    private fun generateId(): String {
        return "tpl_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }

    private fun loadFromStorage() {
        try {
            val json = prefs.getString(KEY_TEMPLATES, null)
            if (!json.isNullOrEmpty()) {
                val type = object : TypeToken<List<WatermarkTemplate>>() {}.type
                val list: List<WatermarkTemplate> = gson.fromJson(json, type)
                list.forEach { templates[it.id] = it }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToStorage() {
        try {
            val json = gson.toJson(templates.values.toList())
            prefs.edit().putString(KEY_TEMPLATES, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
