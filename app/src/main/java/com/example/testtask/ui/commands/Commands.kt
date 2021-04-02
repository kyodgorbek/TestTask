package com.example.testtask.ui.commands

import androidx.annotation.StringRes
import com.example.testtask.data.model.Location
import com.example.testtask.data.model.Step
import com.example.testtask.mvvm.vm.ViewCommand
import com.mapbox.mapboxsdk.geometry.LatLng

sealed class Commands {

	/**
	 * base UI commands
	 * */
	object NetworkError : ViewCommand
	class ShowMessage(@StringRes val resId : Int) : ViewCommand
	class ShowMessageText(val errorMessage : String) : ViewCommand
	object StateLoading : ViewCommand
	object StateEmpty : ViewCommand
	object ReLaunchApp : ViewCommand
	object ShowLoadingDialog : ViewCommand
	object HideLoadingDialog : ViewCommand

	/**
	 * main page UI commands
	 * */
	class DrawPolyline(val list : List<Step>) : ViewCommand
	class AnimateToBBoxAndAddMarkers(var addMarkers : Boolean, val southwest : Location, val northeast : Location, val start : LatLng, val end : LatLng) : ViewCommand
	class ShowStepsList(val list : List<Step>) : ViewCommand
}