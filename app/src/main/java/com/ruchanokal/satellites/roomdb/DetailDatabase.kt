package com.ruchanokal.satellites.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ruchanokal.satellites.model.DetailsModel
import com.ruchanokal.satellites.model.PositionListModel

@Database(entities = [DetailsModel::class,PositionListModel::class], version = 2)
@TypeConverters(Converters::class)
abstract class DetailDatabase : RoomDatabase() {
    abstract fun detailsDao(): DetailsDao
    abstract fun positionListDao(): PositionListDao
}