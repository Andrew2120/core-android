package com.b_labs.network.response

data class ProviderResponseData(
    val isSuccess: Boolean,
    val code: Int,
    val body: Any?,
)
