package com.ruchanokal.satellites.model


data class SatelliteDetailsModel(
    var id : Int,
    var cost_per_launch : Int,
    var first_flight : String,
    var height : Int,
    var mass : Int
)