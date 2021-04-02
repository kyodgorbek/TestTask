package com.example.testtask.data.root

import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ResultFactoryImpl @Inject constructor() : ResultFactory {

	override suspend fun <T> getResult(action : suspend () -> T?) : Flow<Result<T>> {
		return flow {
			emit(Result.makeLoadingResult())

			val result = action()
			result?.let {
				emit(Result.makeSuccessResult<T>(result))
			}
		}.catch {
			if (it is ApiException) {
				emit(Result.makeErrorResult(it.state, it.message, it))
			} else {
				emit(Result.makeErrorResult(UIState.INTERNAL_ERROR))
			}
		}
	}

	override suspend fun <T> getListResult(action : suspend () -> List<T>?) : Flow<Result<List<T>>> {
		return flow {
			emit(Result.makeLoadingResult())

			val result = action()
			if (result.isNullOrEmpty()) {
				emit(Result.makeEmptyResult<List<T>>(null))
			} else {
				emit(Result.makeSuccessResult(result))
			}
		}.catch {
			if (it is ApiException) {
				emit(Result.makeErrorResult(it.state, it.message, it))
			} else {
				emit(Result.makeErrorResult(UIState.INTERNAL_ERROR))
			}
		}
	}

	@FlowPreview
	@ExperimentalCoroutinesApi
	override suspend fun <T : Any> getPagingResult(action : suspend () -> Flow<PagingData<T>>?) : Flow<Result<PagingData<T>>> {
		return channelFlow {
			offer(Result.makeLoadingResult())
			try {
				action()?.collectLatest {
					offer(Result.makeSuccessResult(it))
				}
			} catch (e : ApiException) {
				offer(Result.makeErrorResult<PagingData<T>>(e.state, e.message, e))
			} catch (e : Exception) {
				offer(Result.makeErrorResult<PagingData<T>>(UIState.INTERNAL_ERROR, error = e))
			}
		}
	}
}