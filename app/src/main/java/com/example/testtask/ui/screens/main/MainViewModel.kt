package com.example.testtask.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testtask.Application
import com.example.testtask.data.model.Step
import com.example.testtask.domain.useCase.routes.GetRoutesUseCase
import com.example.testtask.mvvm.vm.BaseViewModel
import com.example.testtask.ui.commands.Commands
import com.example.testtask.ui.screens.main.di.MainComponent
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val KEY = "SL1_5 Brotorpet-Vesslarp.gpx"

class MainViewModel : BaseViewModel() {

	@Inject
	lateinit var useCase : GetRoutesUseCase

	private val _steps : MutableLiveData<List<Step>> = MutableLiveData()
	val steps : LiveData<List<Step>> get() = _steps

	private lateinit var recenterCommand : Commands.AnimateToBBoxAndAddMarkers

	init {
		inject()
		_viewCommands.postValue(Commands.ShowLoadingDialog)
	}

	fun getRoutes() {
		launchDefault {
			val result = useCase.getRouteInfo(KEY)
			result doOnSuccess {
				val legs = it.first().legs.first()
				launchDefault {
					_viewCommands.postValue(Commands.DrawPolyline(legs.steps))
					delay(500)
					_steps.postValue(legs.steps)
					_viewCommands.postValue(
						Commands.AnimateToBBoxAndAddMarkers(
							true,
							it.first().bounds.southwest,
							it.first().bounds.northeast,
							LatLng(legs.start_location.lat, legs.start_location.lng),
							LatLng(legs.end_location.lat, legs.end_location.lng)
						).also { recenterCommand = it }
					)
					delay(500)
					_viewCommands.postValue(Commands.HideLoadingDialog)
				}
			}
		}
	}

	fun recenterRoute(addPins:Boolean){
		_viewCommands.postValue(recenterCommand.apply { addMarkers = addPins })
	}

	override fun inject() {
		Application.getInstance().injectionStorage.get<MainComponent>().inject(this)
	}

	override fun releaseInjection() {
		Application.getInstance().injectionStorage.release<MainComponent>()
	}

	fun showStepsInfo() {
		_viewCommands.postValue(Commands.ShowStepsList(_steps.value ?: emptyList()))
	}
}