package com.example.testtask.data.model

data class RouteResponseModel(
    val bounds: Bounds,
    val copyrights: String,
    val legs: List<Leg>,
    val overview_polyline: Polyline,
    val summary: String,
    val warnings: List<String>,
    val waypoint_order: List<Any>
)