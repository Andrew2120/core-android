package com.b_labs.network.response

sealed class NetworkResponseState<out T, out Y> {
    data class Success<out T, out Y>(val results: T) : NetworkResponseState<T, Y>()
    data class Fail<out T, out Y>(
        val errorType: NetworkCodeError? = null,
        val error: String? = null,
        val errorResponseModel: Y? = null,
    ) : NetworkResponseState<T, Y>()
}
