package com.example.testtask.data.root

import retrofit2.HttpException
import java.net.HttpURLConnection

class ApiException(val state : UIState, message : String?, cause : Throwable?) : Exception(message, cause) {

	val isTokenExpired : Boolean = if (cause is HttpException) {
		cause.code() == HttpURLConnection.HTTP_UNAUTHORIZED || cause.code() == HttpURLConnection.HTTP_FORBIDDEN
	} else {
		false
	}

}