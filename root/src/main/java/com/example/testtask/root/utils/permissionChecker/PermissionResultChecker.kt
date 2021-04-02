package com.example.testtask.root.utils.permissionChecker

interface PermissionResultChecker{
	fun doOnSuccess(block : (granted : String) -> Unit)
	fun doOnFailure(block : (denied : String) -> Unit)
	fun doOnNeverAsk(block : (denied : String) -> Unit)
}