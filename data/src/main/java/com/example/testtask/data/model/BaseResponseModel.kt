package com.example.testtask.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.Response

data class BaseResponseModel<T>(
	val code : Int,
	@SerializedName("is_success")
	val success : Boolean,
	val name : String,
	val description : String,
	val data : T
)

typealias Response<T> = Response<BaseResponseModel<T>>