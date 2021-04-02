package com.example.testtask.root.ext

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.testtask.root.R

fun <T : AppCompatActivity> Fragment.launch(extras : Bundle? = null, flags : Int? = null, block : () -> Class<T>) {
	this.requireActivity().startActivity(Intent(this.requireActivity(), block()).apply {
        flags?.let { this@apply.flags = it }
        extras?.let { putExtras(it) }
    })
}

fun <T : AppCompatActivity> AppCompatActivity.launch(extras : Bundle? = null, flags : Int? = null, block : () -> Class<T>) {
	startActivity(Intent(this, block()).apply {
        flags?.let { this@apply.flags = it }
        extras?.let { putExtras(it) }
    })
}

fun AppCompatActivity.withFullScreen() {
	window.apply {
		decorView.systemUiVisibility = if (Build.VERSION.SDK_INT > 27) {
			navigationBarColor = ContextCompat.getColor(this@withFullScreen, R.color.dark_blue_base)
			navigationBarDividerColor = ContextCompat.getColor(this@withFullScreen, R.color.divider_color)
			statusBarColor = ContextCompat.getColor(this@withFullScreen, android.R.color.transparent)
			View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		} else {
			navigationBarColor = ContextCompat.getColor(this@withFullScreen, android.R.color.black)
			if (Build.VERSION.SDK_INT > 22) {
				statusBarColor = ContextCompat.getColor(this@withFullScreen, android.R.color.transparent)
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			} else {
				statusBarColor = ContextCompat.getColor(this@withFullScreen, android.R.color.black)
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			}
		}
	}
}

fun AppCompatActivity.setLightStatusBar(@ColorRes statusColor : Int = android.R.color.white) {
	window.apply {
		if (Build.VERSION.SDK_INT > 22) {
			decorView.systemUiVisibility = if (Build.VERSION.SDK_INT > 27) {
				navigationBarColor = ContextCompat.getColor(
                    this@setLightStatusBar,
                    android.R.color.white
                )
				navigationBarDividerColor = ContextCompat.getColor(
                    this@setLightStatusBar,
                    R.color.divider_color
                )
				decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
			} else {
				navigationBarColor = ContextCompat.getColor(
                    this@setLightStatusBar,
                    android.R.color.black
                )
				decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
			}
			statusBarColor = ContextCompat.getColor(this@setLightStatusBar, statusColor)
		} else {
			statusBarColor = ContextCompat.getColor(this@setLightStatusBar, android.R.color.black)
		}
	}
}

fun AppCompatActivity.clearLightStatusBar() {
	window?.apply {
		if (Build.VERSION.SDK_INT > 22) {
			decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
			statusBarColor = ContextCompat.getColor(
                this@clearLightStatusBar,
                android.R.color.transparent
            )
		} else {
			statusBarColor = ContextCompat.getColor(this@clearLightStatusBar, android.R.color.black)
		}
	}
}
