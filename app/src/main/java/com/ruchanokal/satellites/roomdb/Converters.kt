package com.ruchanokal.satellites.roomdb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ruchanokal.satellites.model.PositionListModel

class Converters {

    @TypeConverter
    fun fromGroupTaskMemberList(value: List<PositionListModel>): String {
        val gson = Gson()
        val type = object : TypeToken<List<PositionListModel>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGroupTaskMemberList(value: String): List<PositionListModel> {
        val gson = Gson()
        val type = object : TypeToken<List<PositionListModel>>() {}.type
        return gson.fromJson(value, type)
    }
}