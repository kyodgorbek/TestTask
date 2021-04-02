package com.example.testtask.data.model

data class Leg(
	val distance: TextValuePair,
	val duration: TextValuePair,
	val end_address: String,
	val end_location: Location,
	val start_address: String,
	val start_location: Location,
	val steps: List<Step>,
	val traffic_speed_entry: List<Any>,
	val via_waypoint: List<Any>
)