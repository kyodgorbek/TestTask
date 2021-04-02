package com.example.testtask.data.root

data class Result<R>(
    val uiState: UIState,
    val data: R?,
    val msg: String?,
    val error: Throwable?
) {

    companion object {
        fun <R> makeSuccessResult(data: R) =
            Result(UIState.SUCCESS, data, null, null)

        fun <E> makeErrorResult(state: UIState, msg: String? = null, error: Throwable? = null) =
            Result<E>(state, null, msg, error)

        inline fun <reified E> makeEmptyResult(message: String? = null) =
            Result(UIState.EMPTY, null as? E, message, null)

        fun <L> makeLoadingResult(message: String? = null) =
            Result<L>(UIState.LOADING, null, message, null)

    }
}