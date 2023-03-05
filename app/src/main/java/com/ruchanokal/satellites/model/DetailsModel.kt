package com.ruchanokal.satellites.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DetailsModel(
                        @ColumnInfo(name = "id")
                        var id : Int,
                        @ColumnInfo(name = "name")
                        var name: String,
                        @ColumnInfo(name = "first_flight")
                        var firstFlight: String,
                        @ColumnInfo(name = "heightMass")
                        var heightMass : String,
                        @ColumnInfo(name = "cost")
                        var cost : String,
                        @ColumnInfo(name = "position_list")
                        var positions : List<PositionListModel>) {
    @PrimaryKey(autoGenerate = true)
    var id2 = 0
}