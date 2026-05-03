package com.thisthatdc.calorytracker.util

import com.thisthatdc.calorytracker.data.food.Food
import com.thisthatdc.calorytracker.data.settings.SettingsState
import kotlinx.coroutines.flow.Flow
import java.io.OutputStream

class JsonWriter {

    companion object {
        val MAGIC_NUMBER = byteArrayOf(0xF0.toByte(), 0x9F.toByte(), 0x96.toByte(), 0x95.toByte())
        val CHARSET = Charsets.UTF_8
        fun write(stream: OutputStream, settings: SettingsState, all: Flow<List<Food>>) {
            stream.write(MAGIC_NUMBER)
            stream.write("{".toByteArray(CHARSET))
            stream.flush()
            writeSettings(stream, settings)
            writeFood(stream, all)
            stream.write("}".toByteArray(CHARSET))
            stream.flush()
        }

        private fun writeFood(stream: OutputStream, all:Flow<List<Food>>) {

        }

        private fun writeSettings(stream: OutputStream, settings: SettingsState) {
            stream.write("\"settings:\"".toByteArray(CHARSET))
            val json = buildString {
                append("{")
                append("\"calories\":")
                append(settings.calories)
                append(",\"protein\":")
                append(settings.protein)
                append(",\"fat\":")
                append(settings.fat)
                append(",\"carbs\":")
                append(settings.carbs)
                append("}")
            }
            stream.write(json.toByteArray(CHARSET))
            stream.flush()
        }
    }
}