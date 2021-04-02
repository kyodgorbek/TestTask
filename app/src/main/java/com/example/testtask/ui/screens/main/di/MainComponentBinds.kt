package com.example.testtask.ui.screens.main.di

import com.example.testtask.domain.useCase.routes.GetRoutesUseCase
import com.example.testtask.domain.useCase.routes.GetRoutesUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface MainComponentBinds {

	@Binds
	fun bindsTransactionsPerSecondUseCase(getTransactionsPerDayUseCaseImpl : GetRoutesUseCaseImpl) : GetRoutesUseCase

}