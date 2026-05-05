package com.thisthatdc.calorytracker.util

import com.thisthatdc.calorytracker.data.food.Food
import com.thisthatdc.calorytracker.data.food.FoodEaten
import com.thisthatdc.calorytracker.data.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.OutputStream

class JsonWriter {

    companion object {
        val MAGIC_NUMBER = byteArrayOf(0xF0.toByte(), 0x9F.toByte(), 0x96.toByte(), 0x95.toByte())
        val CHARSET = Charsets.UTF_8
        fun write(stream: OutputStream, settings: Flow<Settings?>, food: Flow<List<Food>>, foodEaten: Flow<List<FoodEaten?>>) {
            stream.write(MAGIC_NUMBER)
            stream.write("{".toByteArray(CHARSET))
            stream.flush()
            writeSettings(stream, settings)
            writeFood(stream, food)
            writeFoodEaten(stream, foodEaten)
            stream.write("}".toByteArray(CHARSET))
            stream.flush()
        }

        private fun writeFood(stream: OutputStream, food: Flow<List<Food>>) {
            val foods = runBlocking { food.first() }
            val json = Json.encodeToString(foods)
            stream.write(",\"food\":".toByteArray(CHARSET))
            stream.write(json.toByteArray(CHARSET))
            stream.flush()
        }

        private fun writeFoodEaten(stream: OutputStream, food: Flow<List<FoodEaten?>>) {
            val foods = runBlocking { food.first() }
            val json = Json.encodeToString(foods)
            stream.write(",\"foodEaten\":".toByteArray(CHARSET))
            stream.write(json.toByteArray(CHARSET))
            stream.flush()
        }

        private fun writeSettings(stream: OutputStream, settings: Flow<Settings?>) {
            val foods = runBlocking { settings.first() }
            val json = Json.encodeToString(foods)
            stream.write("\"settings\":".toByteArray(CHARSET))
            stream.write(json.toByteArray(CHARSET))
            stream.flush()
        }
    }
}
