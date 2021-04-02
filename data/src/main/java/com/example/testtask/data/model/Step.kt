package com.example.testtask.data.model

import android.os.Parcelable
import com.example.testtask.root.utils.DifItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
	val distance : TextValuePair,
	val duration : TextValuePair,
	val end_location : Location,
	val html_instructions : String,
	val maneuver : String?,
	val polyline : Polyline,
	val start_location : Location,
	val travel_mode : String
) : Parcelable, DifItem<Step> {

	override fun areItemsTheSame(second : Step) : Boolean {
		return html_instructions == second.html_instructions
	}

	override fun areContentsTheSame(second : Step) : Boolean {
		return travel_mode == second.travel_mode &&
				html_instructions == second.html_instructions &&
				start_location.lat == start_location.lat &&
				start_location.lng == start_location.lng &&
				end_location.lng == end_location.lng &&
				end_location.lng == end_location.lng
	}

}