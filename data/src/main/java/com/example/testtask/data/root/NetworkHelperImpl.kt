package com.example.testtask.data.root

import android.content.Context
import android.util.Log
import com.example.testtask.data.model.Response
import com.example.testtask.root.di.ApplicationContext
import com.example.testtask.root.utils.NetworkConnectivityChecker
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NetworkHelperImpl
@Inject
constructor(
	@ApplicationContext private val context : Context,
	private val connectivityChecker : NetworkConnectivityChecker
) : NetworkHelper {

	private val defaultInternalErrorMessage : String by lazy { context.getString(com.example.testtask.data.R.string.default_internal_error_message) }
	private val defaultNetworkErrorMessage : String by lazy { context.getString(com.example.testtask.data.R.string.default_network_error_message) }

	override suspend fun <R> proceed(action : suspend () -> Response<R>) : R? = callInternal { action() }

	private suspend fun <R> callInternal(action : suspend () -> Response<R>) : R? {
		return if (!connectivityChecker.isNetworkAvailable) {
			throw ApiException(UIState.NETWORK_ERROR, defaultNetworkErrorMessage, null)
		} else {
			val response = action()
			val responseBody = response.body()
			Log.d("Response",response.toString())
			when {
				!response.isSuccessful -> {
					val message = try {
						JSONObject(response.errorBody()?.string().toString()).getJSONArray("message").get(0).toString()
					} catch (e : IOException) {
						defaultInternalErrorMessage
					}
					throw ApiException(
						UIState.SERVER_ERROR,
						message,
						HttpException(response)
					)
				}
				responseBody?.success == false -> {
					throw ApiException(
						UIState.SERVER_ERROR,
						defaultInternalErrorMessage,
						null
					)
				}
				responseBody == null -> throw ApiException(
					UIState.SERVER_ERROR,
					defaultInternalErrorMessage,
					null
				)
				else -> responseBody.data
			}
		}
	}

}