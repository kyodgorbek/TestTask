package com.example.testtask.mvvm.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.testtask.R
import com.example.testtask.data.root.ApiException
import com.example.testtask.data.root.Result
import com.example.testtask.data.root.UIState
import com.example.testtask.root.utils.SingleLiveData
import com.example.testtask.ui.commands.Commands
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

	protected val _viewCommands = SingleLiveData<ViewCommand>()
	val viewCommands : LiveData<ViewCommand>
		get() = _viewCommands

	protected val _uiState = SingleLiveData<UIState>().apply { postValue(UIState.LOADING) }
	val uiState : LiveData<UIState>
		get() = _uiState

	override val coroutineContext : CoroutineContext
		get() = Job() + Dispatchers.Default

	protected abstract fun inject()
	protected abstract fun releaseInjection()

	fun switchUIState(state : UIState) {
		_uiState.postValue(UIState.SUCCESS)
	}

	fun CoroutineScope.launchIO(block : suspend () -> Unit) {
		launch(context = Dispatchers.IO) { block() }
	}

	fun CoroutineScope.launchMain(block : suspend () -> Unit) {
		launch(context = Dispatchers.Main) { block() }
	}

	fun CoroutineScope.launchDefault(block : suspend () -> Unit) {
		launch(context = Dispatchers.Default) { block() }
	}

	protected suspend infix fun <T> Flow<Result<T>>.doOnSuccess(doOnSuccess : (T) -> Unit) {
		collect {
			_uiState.postValue(it.uiState)
			when (it.uiState) {
				UIState.SUCCESS -> it.data?.let(doOnSuccess)
				UIState.LOADING -> _viewCommands.postValue(Commands.StateLoading)
				UIState.EMPTY -> _viewCommands.postValue(Commands.StateEmpty)
				UIState.NETWORK_ERROR -> _viewCommands.postValue(Commands.NetworkError)
				UIState.INTERNAL_ERROR -> _viewCommands.postValue(Commands.ShowMessage(R.string.default_internal_error_message))
				UIState.SERVER_ERROR -> if (it.error is ApiException && (it.error as ApiException).isTokenExpired) {
//					_viewCommands.postValue(Commands.ReLaunchApp)
					_viewCommands.postValue(Commands.ShowMessageText("Unauthorized"))
				} else {
					_viewCommands.postValue(Commands.ShowMessageText(it.msg ?: ""))
				}
			}
		}
	}

	override fun onCleared() {
		super.onCleared()
		releaseInjection()
	}
}