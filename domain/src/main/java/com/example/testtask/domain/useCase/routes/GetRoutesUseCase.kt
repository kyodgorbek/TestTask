package com.example.testtask.domain.useCase.routes

import com.example.testtask.data.model.RouteResponseModel
import com.example.testtask.data.root.Result
import kotlinx.coroutines.flow.Flow

interface GetRoutesUseCase {

	suspend fun getRouteInfo(apiKey:String) : Flow<Result<List<RouteResponseModel>>>

}