package com.smartwardrobe.app.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject

object WardrobeRepository {
    val items = mutableStateListOf<ClothingItem>()
    var clothingTypes = listOf<String>()
        private set
    var colors = listOf<String>()
        private set

    private const val PREFS_NAME = "wardrobe_prefs"
    private const val KEY_ITEMS = "items"

    // Добавляем вещь и сохраняем
    fun addItem(item: ClothingItem, context: Context) {
        items.add(item)
        saveWardrobe(context)
    }

    // Удаляем вещь и сохраняем
    fun removeItem(item: ClothingItem, context: Context) {
        items.remove(item)
        saveWardrobe(context)
    }

    // Загрузка типов одежды из JSON
    fun loadClothingTypes(context: Context) {
        val jsonString = context.assets.open("ClothingTypes.json")
            .bufferedReader()
            .use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("types")
        clothingTypes = List(jsonArray.length()) { i -> jsonArray.getString(i) }
    }

    // Загрузка цветов из JSON
    fun loadColors(context: Context) {
        val jsonString = context.assets.open("Colors.json")
            .bufferedReader()
            .use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("colors")
        colors = List(jsonArray.length()) { i -> jsonArray.getString(i) }
    }

    // Сохраняем список вещей в SharedPreferences
    private fun saveWardrobe(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonArray = JSONArray()
        items.forEach {
            val obj = JSONObject()
            obj.put("type", it.type)
            obj.put("color", it.color)
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_ITEMS, jsonArray.toString()).apply()
    }

    // Загружаем список вещей из SharedPreferences
    fun loadWardrobe(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_ITEMS, null) ?: return
        val jsonArray = JSONArray(jsonString)
        items.clear()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            items.add(ClothingItem(obj.getString("type"), obj.getString("color")))
        }
    }

    // Загрузка всех данных
    fun loadAll(context: Context) {
        loadClothingTypes(context)
        loadColors(context)
        loadWardrobe(context)
    }
}
