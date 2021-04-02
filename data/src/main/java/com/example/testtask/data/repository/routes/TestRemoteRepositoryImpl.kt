package com.example.testtask.data.repository.routes

import com.example.testtask.data.model.RouteResponseModel
import com.example.testtask.data.root.NetworkHelper
import com.example.testtask.data.services.ITestService
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

class TestRemoteRepositoryImpl
@Inject
constructor(
	private val networkHelper : NetworkHelper,
	retrofit : Retrofit
) : TestRemoteRepository {

	private val mService : ITestService = retrofit.create()

	override suspend fun getAll(apiKey : String) : List<RouteResponseModel>? {
		return networkHelper.proceed {
			mService.getRoutes(JsonObject().apply {
				addProperty("query", apiKey)
			})
		}
	}
}