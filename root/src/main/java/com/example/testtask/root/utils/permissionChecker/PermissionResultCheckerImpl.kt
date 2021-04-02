package com.example.testtask.root.utils.permissionChecker

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class PermissionResultCheckerImpl : PermissionResultChecker {

	private var doOnSuccess : ((granted : String) -> Unit)? = null
	private var doOnFailure : ((denied : String) -> Unit)? = null
	private var doOnNeverAsk : ((denied : String) -> Unit)? = null

	override fun doOnSuccess(block : (granted : String) -> Unit) {
		doOnSuccess = block
	}

	override fun doOnFailure(block : (denied : String) -> Unit) {
		doOnFailure = block
	}

	override fun doOnNeverAsk(block : (denied : String) -> Unit) {
		doOnNeverAsk = block
	}

	internal fun getCallback() = object : PermissionFragment.Callback {
		override fun permissionGranted(granted : String) {
			doOnSuccess?.invoke(granted)
		}

		override fun permissionDenied(denied : String) {
			doOnFailure?.invoke(denied)
		}

		override fun permissionDeniedWithNeverAsk(denied : String) {
			doOnNeverAsk?.invoke(denied)
		}
	}
}