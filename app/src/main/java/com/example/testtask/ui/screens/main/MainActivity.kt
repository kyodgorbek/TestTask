package com.example.testtask.ui.screens.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.example.testtask.R
import com.example.testtask.data.model.Location
import com.example.testtask.data.model.Step
import com.example.testtask.databinding.ActivityMainBinding
import com.example.testtask.mvvm.ui.BaseRequestActivity
import com.example.testtask.mvvm.vm.ViewCommand
import com.example.testtask.root.ext.dpToPx
import com.example.testtask.root.ext.show
import com.example.testtask.root.utils.permissionChecker.permissionChecker
import com.example.testtask.ui.commands.Commands
import com.example.testtask.ui.screens.main.fragments.StepInfoBottomFragment
import com.google.android.gms.location.LocationServices
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Line
import com.mapbox.mapboxsdk.plugins.annotation.LineManager
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions
import com.mapbox.mapboxsdk.utils.ColorUtils
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseRequestActivity<ActivityMainBinding, MainViewModel>() {

	override val inflate : (LayoutInflater) -> ActivityMainBinding
		get() = ActivityMainBinding::inflate
	override val viewModelType : Class<MainViewModel>
		get() = MainViewModel::class.java

	private var mMap : MapboxMap? = null
	private lateinit var lineManager : LineManager
	private val lines = arrayListOf<Line>()
	private var navigationMapRoute : NavigationMapRoute? = null
	private var currentRoute : DirectionsRoute? = null

	override fun onCreate(savedInstanceState : Bundle?) {
		Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
		super.onCreate(savedInstanceState)
		mBinding.apply {
			mapView.onCreate(savedInstanceState)
		}
	}

	@SuppressLint("MissingPermission")
	override fun initView(binding : ActivityMainBinding, viewModel : MainViewModel) {
		mBinding.apply {
			mapView.getMapAsync { map ->
				map.setStyle(Style.DARK) { style ->
					mMap = map
					mViewModel.getRoutes()
					initClicks()
					map.uiSettings.isLogoEnabled = false
					map.uiSettings.setCompassMargins(0, 100.dpToPx(), 20.dpToPx(), 0)
					lineManager = LineManager(mapView, map, style)
				}
			}
		}
	}

	override fun proceedViewCommand(command : ViewCommand) {
		when (command) {
			is Commands.DrawPolyline -> drawPolyline(command.list)
			is Commands.AnimateToBBoxAndAddMarkers -> {
				animateToBBox(command.southwest, command.northeast)
				addMarkers(command.start, command.end)
				getRoute(Point.fromLngLat(command.start.longitude, command.start.latitude), Point.fromLngLat(command.end.longitude, command.end.latitude))
			}
			is Commands.ShowLoadingDialog -> showLoadingDialog()
			is Commands.HideLoadingDialog -> hideLoadingDialog()
			is Commands.ShowStepsList -> StepInfoBottomFragment.show(supportFragmentManager,command.list)
		}
	}

	private fun ActivityMainBinding.initClicks() {
		tvRetry.setOnClickListener {
			val options = NavigationLauncherOptions.builder()
				.directionsRoute(currentRoute)
				.shouldSimulateRoute(true)
				.build()
			NavigationLauncher.startNavigation(this@MainActivity, options)
		}
		fabMyLocation.setOnClickListener {
			checkPermission {
				findMyLocation { myLocation ->
					mMap?.let { map ->
						map.style?.let { style ->
							val locationComponent : LocationComponent = map.locationComponent
							locationComponent.activateLocationComponent(
								LocationComponentActivationOptions.builder(this@MainActivity, style).build()
							)
							locationComponent.isLocationComponentEnabled = true
							locationComponent.cameraMode = CameraMode.TRACKING
							locationComponent.renderMode = RenderMode.COMPASS
						}
					}

					mMap?.animateCamera(
						CameraUpdateFactory.newLatLngZoom(myLocation, 15.0), 1000
					)
					showToast("Your location is: ${myLocation.latitude}, ${myLocation.longitude}")
				}
			}
		}
		tvRecenter.setOnClickListener { mViewModel.recenterRoute(false) }
		tvSeeSteps.setOnClickListener { mViewModel.showStepsInfo() }
	}

	private fun ActivityMainBinding.showActions() {
		val set2 = ConstraintSet()
		set2.clone(rootLayout.also {
			clActions.show()
			flSteps.show()
		})
		val transition : Transition = ChangeBounds()
		transition.interpolator = OvershootInterpolator()
		transition.duration = 500
		set2.applyTo(rootLayout)
		TransitionManager.beginDelayedTransition(rootLayout, transition)
	}

	private fun getRoute(origin : Point, destination : Point) {
		mBinding.tvRetry.isEnabled = false
		mBinding.tvRetry.isClickable = false
		NavigationRoute.builder(this)
			.accessToken(Mapbox.getAccessToken()!!)
			.origin(origin)
			.destination(destination)
			.build()
			.getRoute(object : Callback<DirectionsResponse?> {
				override fun onResponse(call : Call<DirectionsResponse?>?, response : Response<DirectionsResponse?>) {
					if (response.body() == null) {
						return
					} else if ((response.body()?.routes()?.size ?: 0) < 1) {
						return
					}
					currentRoute = response.body()?.routes()?.get(0)

					if (navigationMapRoute != null) {
						//alternative way wasn't found
						navigationMapRoute?.removeRoute()
					} else {
						mMap?.let {
							navigationMapRoute = NavigationMapRoute(null, mBinding.mapView, it, R.style.NavigationMapRoute)
						}
					}
					navigationMapRoute?.addRoute(currentRoute)
					mBinding.tvRetry.isEnabled = true
					mBinding.tvRetry.isClickable = true
					mBinding.showActions()
				}

				override fun onFailure(call : Call<DirectionsResponse?>?, throwable : Throwable) {
				}
			})
	}

	private fun checkPermission(action : () -> Unit) {
		permissionChecker {
			permissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
			check {
				doOnSuccess {
					action()
				}
				doOnFailure { showToast("For correct working we need the permission to your location.") }
				doOnNeverAsk { showToast("For correct working we need the permission to your location.") }
			}
		}
	}

	@SuppressLint("MissingPermission")
	private fun findMyLocation(action : (LatLng) -> Unit) {
		LocationServices.getFusedLocationProviderClient(this)
			.lastLocation.addOnSuccessListener {
				it?.let { action(LatLng(it.latitude, it.longitude)) }
			}
	}

	private fun animateToBBox(southwest : Location, northeast : Location) {
		mMap?.let { map ->
			val latLngBounds = LatLngBounds.Builder()
			latLngBounds.include(LatLng(southwest.lat, southwest.lng))
			latLngBounds.include(LatLng(northeast.lat, northeast.lng))
			map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 50), 1000)
		}
	}

	private fun addMarkers(start : LatLng, end : LatLng) {
		mMap?.let { map ->
			//alternative way wasn't found
			map.addMarker(MarkerOptions().position(LatLng(start.latitude, start.longitude)))
			map.addMarker(MarkerOptions().position(LatLng(end.latitude, end.longitude)))
		}
	}

	private fun drawPolyline(list : List<Step>) {
		list.forEach {
			val points = PolylineUtils.decode(it.polyline.points, 5).map { point ->
				LatLng(point.latitude(), point.longitude())
			}
			val lineOptions = LineOptions()
				.withLatLngs(points)
				.withLineColor(ColorUtils.colorToRgbaString(Color.RED))
				.withLineWidth(3.0f)
			lines.add(lineManager.create(lineOptions))
		}
	}

	override fun onStart() {
		super.onStart()
		mBinding.mapView.onStart()
	}

	override fun onStop() {
		super.onStop()
		mBinding.mapView.onStop()
	}

	override fun onResume() {
		super.onResume()
		mBinding.mapView.onResume()
	}

	override fun onLowMemory() {
		super.onLowMemory()
		mBinding.mapView.onLowMemory()
	}

	override fun onPause() {
		super.onPause()
		mBinding.mapView.onPause()
	}

	override fun onDestroy() {
		super.onDestroy()
		mBinding.mapView.onDestroy()
		if(::lineManager.isInitialized){
			lineManager.onDestroy()
		}
	}

	override fun onSaveInstanceState(outState : Bundle) {
		super.onSaveInstanceState(outState)
		mBinding.mapView.onSaveInstanceState(outState)
	}
}