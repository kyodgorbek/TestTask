package com.example.testtask.data.services

import com.example.testtask.data.model.Response
import com.example.testtask.data.model.RouteResponseModel
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface ITestService {
	@POST("navigate-inside-trail")
	suspend fun getRoutes(@Body jsonObject : JsonObject) : Response<List<RouteResponseModel>>
}