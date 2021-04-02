package com.example.testtask.data.repository.routes

import com.example.testtask.data.model.RouteResponseModel

interface TestRemoteRepository {

	suspend fun getAll(apiKey:String) : List<RouteResponseModel>?

}