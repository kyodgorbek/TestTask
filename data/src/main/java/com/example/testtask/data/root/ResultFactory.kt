package com.example.testtask.data.root

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ResultFactory {

    suspend fun <T> getResult(action: suspend () -> T?): Flow<Result<T>>

    suspend fun <T> getListResult(action: suspend () -> List<T>?): Flow<Result<List<T>>>

    suspend fun <T : Any> getPagingResult(action: suspend () -> Flow<PagingData<T>>?): Flow<Result<PagingData<T>>>

}