package com.example.testtask.ui.screens.splash

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.testtask.root.ext.launch

class SplashScreenFirstActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)
        launch { SplashScreenSecondActivity::class.java }
        finish()
    }
}