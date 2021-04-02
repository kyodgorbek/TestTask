package com.example.testtask.root.utils.permissionChecker

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

internal class PermissionFragment : Fragment() {

	interface Callback {

		fun permissionGranted(granted : String)
		fun permissionDenied(denied : String)
		fun permissionDeniedWithNeverAsk(denied : String)
	}

	private lateinit var resultHandler : Callback
	private var onFinishRequest : (() -> Unit)? = null

	companion object {

		private const val PERMISSIONS_REQUEST_CODE = 74

		fun newInstance() = PermissionFragment()

	}

	fun requestPermissions(permissions : Array<out String>, resultHandler : Callback, onFinishRequest : () -> Unit) {
		this.resultHandler = resultHandler
		this.onFinishRequest = onFinishRequest
		requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
	}

	override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<out String>, grantResults : IntArray) {
		if (requestCode == PERMISSIONS_REQUEST_CODE) {
			permissions.forEachIndexed { index, permission ->
				if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
					resultHandler.permissionGranted(permission)
				} else {
					if (shouldShowRequestPermissionRationale(permission)) {
						resultHandler.permissionDenied(permission)
					} else {
						resultHandler.permissionDeniedWithNeverAsk(permission)
					}
				}
			}
			onFinishRequest?.invoke()
		}
	}
}