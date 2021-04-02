package com.example.testtask.di

import com.example.testtask.Application
import com.example.testtask.data.di.NetworkModule
import com.example.testtask.root.di.RootModule
import com.example.testtask.ui.screens.main.di.MainComponent
import kotlin.reflect.KClass

class DependencyInjectionStorage private constructor(private val app : Application) {

	companion object {

		fun initialize(app : Application) : DependencyInjectionStorage {
			return DependencyInjectionStorage(app)
		}
	}

	private val componentMap = mutableMapOf<KClass<*>, Any>()

	private val rootModule : RootModule = RootModule(app)
	private val netModule = NetworkModule()

	inline fun <reified T> get() : T = getComponent(T::class)

	@Suppress("UNCHECKED_CAST")
	fun <T> getComponent(kClass : KClass<*>) : T {
		val component = componentMap[kClass]
		return if (component == null) {
			val newComponent = provideComponent(kClass)
			componentMap[kClass] = newComponent
			newComponent as T
		} else {
			component as T
		}
	}

	private inline fun <reified T> getBase() : T = getBaseComponent(T::class)

	@Suppress("UNCHECKED_CAST")
	private fun <T> getBaseComponent(kClass : KClass<*>) : T {
		val component = componentMap[kClass]
		return if (component == null) {
			val newComponent = provideComponent(kClass)
			componentMap[kClass] = newComponent
			newComponent as T
		} else {
			component as T
		}
	}

	inline fun <reified T> release() = releaseComponent(T::class)

	fun releaseComponent(kClass : KClass<*>) {
		val component = componentMap[kClass]
		component?.let {
			componentMap.remove(kClass)
		} ?: return
	}

	private fun provideComponent(kClass : KClass<*>) : Any {
		return when (kClass) {
			AppComponent::class -> DaggerAppComponent.builder().withRootModule(rootModule).withNetworkModule(netModule).build()
			MainComponent::class -> getBase<AppComponent>().mainComponent.build()
			else -> throw UnsupportedOperationException("This component is not supported: ${kClass.simpleName}")
		}
	}

}