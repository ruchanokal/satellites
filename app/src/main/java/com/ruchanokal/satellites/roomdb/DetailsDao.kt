package com.ruchanokal.satellites.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ruchanokal.satellites.model.DetailsModel
import com.ruchanokal.satellites.model.PositionListModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface DetailsDao {

    @Query("SELECT * FROM DetailsModel WHERE id = (:id)")
    fun getDataById(id: Int): DetailsModel

    @Query("SELECT * FROM DetailsModel")
    fun getData(): DetailsModel

    @Insert
    fun insert(detailsModel: DetailsModel)

}