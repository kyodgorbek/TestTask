package com.example.testtask.root.utils.permissionChecker

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.permissionChecker(block : PermissionChecker.() -> Unit) {
	PermissionCheckerImpl().apply {
		withActivity(this@permissionChecker)
		block(this)
	}
}

fun Fragment.permissionChecker(block : PermissionChecker.() -> Unit) {
	PermissionCheckerImpl().apply {
		withFragment(this@permissionChecker)
		block(this)
	}
}

interface PermissionChecker {

	fun permissions(vararg permissions : String)

	fun check(block : PermissionResultChecker.() -> Unit)

}