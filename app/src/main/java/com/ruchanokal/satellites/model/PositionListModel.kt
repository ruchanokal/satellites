package com.ruchanokal.satellites.model



import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PositionListModel(
    @SerializedName("posX")
    @ColumnInfo(name = "pos_x")
    val posX: Double,
    @SerializedName("posY")
    @ColumnInfo(name = "pos_y")
    val posY: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}