package com.example.testtask.data.root

import com.example.testtask.data.model.Response

interface NetworkHelper {

    @DslMarker
    annotation class ApiRequest

    @ApiRequest
    suspend fun <R> proceed(action: suspend () -> Response<R>): R?

}