package com.example.testtask.domain.useCase.routes

import com.example.testtask.data.model.RouteResponseModel
import com.example.testtask.data.repository.routes.TestRemoteRepository
import com.example.testtask.data.root.Result
import com.example.testtask.data.root.ResultFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoutesUseCaseImpl
@Inject
constructor(
	private val resultFactory : ResultFactory,
	private val testRemoteRepository : TestRemoteRepository
) : GetRoutesUseCase {

	override suspend fun getRouteInfo(apiKey:String) : Flow<Result<List<RouteResponseModel>>> {
		return resultFactory.getListResult { testRemoteRepository.getAll(apiKey) }
	}
}