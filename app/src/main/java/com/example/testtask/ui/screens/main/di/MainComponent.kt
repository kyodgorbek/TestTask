package com.example.testtask.ui.screens.main.di

import com.example.testtask.ui.screens.main.MainViewModel
import dagger.Subcomponent

@Subcomponent(modules = [MainComponentBinds::class])
interface MainComponent {

	@Subcomponent.Builder
	interface Builder{
		fun build() : MainComponent

	}

	fun inject(target:MainViewModel)

}