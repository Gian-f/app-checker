package com.br.appchecker.data.local.converter

import androidx.room.TypeConverter
import com.br.appchecker.data.remote.response.AnswersData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromJson(json: String): List<AnswersData> {
        val typeToken = object : TypeToken<List<AnswersData>>() {}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun toJson(answersData: List<AnswersData>): String {
        return Gson().toJson(answersData)
    }
}