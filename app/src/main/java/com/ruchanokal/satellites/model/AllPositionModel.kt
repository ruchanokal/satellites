package com.ruchanokal.satellites.model



import com.google.gson.annotations.SerializedName

data class AllPositionModel(
    @SerializedName("list")
    val list: List<TopPositionModel>
)