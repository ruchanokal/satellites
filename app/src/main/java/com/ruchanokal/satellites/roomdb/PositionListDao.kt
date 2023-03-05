package com.ruchanokal.satellites.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ruchanokal.satellites.model.PositionListModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface PositionListDao {

    @Query("SELECT * FROM PositionListModel")
    fun getAll(): List<PositionListModel>

    @Insert
    fun insert(positionListModel: List<PositionListModel>)

}