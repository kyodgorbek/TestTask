package com.example.testtask.root.utils.permissionChecker

import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class PermissionCheckerImpl : PermissionChecker {

	private var activity : AppCompatActivity? = null
	private var fragment : Fragment? = null
	private var permissions: Array<out String>? = null

	internal fun withActivity(activity : AppCompatActivity) {
		this.activity = activity
	}

	internal fun withFragment(fragment : Fragment) {
		this.fragment = fragment
	}

	override fun permissions(vararg permissions : String) {
		this.permissions = permissions
	}

	override fun check(block : PermissionResultChecker.() -> Unit) {
		val fm : FragmentManager = activity?.supportFragmentManager
			?: fragment?.childFragmentManager
			?: throw IllegalStateException("For correct working you need to pass Fragment or FragmentActivity")
		val fragment = PermissionFragment.newInstance()
		val resultCheckerImpl = PermissionResultCheckerImpl().apply(block)
		fm.beginTransaction()
			.add(fragment, PermissionFragment::class.java.simpleName)
			.commitNow()
		permissions?.let {
			fragment.requestPermissions(it, resultCheckerImpl.getCallback()) {
				fm.beginTransaction().remove(fragment)
			}
		}?: throw IllegalStateException("Permissions to request not found")
	}

}