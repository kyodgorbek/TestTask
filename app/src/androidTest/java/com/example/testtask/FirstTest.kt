package com.example.testtask

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirstTest {

	private lateinit var device:UiDevice

	@Before
	fun prepareDevice(){
		device = UiDevice.getInstance(getInstrumentation())
	}

	@Test
	fun presHome3Times(){
		GlobalScope.launch {
			device.pressBack()
			delay(3000)
			device.pressBack()
			delay(3000)
			device.pressBack()
			delay(3000)
			device.pressBack()
			delay(3000)
			device.pressBack()
			delay(3000)
			device.pressBack()
			delay(3000)

		}
	}

}