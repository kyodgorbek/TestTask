package com.example.testtask.ui.screens.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.example.testtask.Application
import com.example.testtask.databinding.ActivitySplashScreenBinding
import com.example.testtask.root.di.RootModule
import com.example.testtask.root.utils.SharedPreferencesHelper
import com.example.testtask.ui.screens.main.MainActivity
import com.example.testtask.ui.screens.splash.di.DaggerSplashScreenComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenSecondActivity : AppCompatActivity() {

	@Inject
	lateinit var mShared : SharedPreferencesHelper

	override fun onCreate(savedInstanceState : Bundle?) {
		window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
		super.onCreate(savedInstanceState)
		DaggerSplashScreenComponent.builder()
			.rootModule(RootModule(Application.getInstance()))
			.build().inject(this)
		val mBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
		setContentView(mBinding.root)
		lifecycleScope.launch {
			delay(2000)
			val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashScreenSecondActivity, mBinding.appIcon, mBinding.appIcon.transitionName)
			startActivity(Intent(this@SplashScreenSecondActivity, MainActivity::class.java), options.toBundle())
			delay(1500)
			finishAfterTransition()
		}
	}
}