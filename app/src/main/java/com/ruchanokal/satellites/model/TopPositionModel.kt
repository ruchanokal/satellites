package com.ruchanokal.satellites.model



import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class TopPositionModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("positions")
    val positions: List<PositionListModel>
)