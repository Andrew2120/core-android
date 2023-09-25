package com.b_labs.network.response

object NetworkUtils {
    fun getNetworkErrorType(code: Int): NetworkCodeError {
        return when (code) {
            400 -> NetworkCodeError.BAD_REQUEST

            401 -> NetworkCodeError.UNAUTHORIZED

            403 -> NetworkCodeError.FORBIDDEN

            404 -> NetworkCodeError.NOT_FOUND

            503 -> NetworkCodeError.SERVER_NOT_REACHABLE

            500 -> NetworkCodeError.SERVER_NOT_REACHABLE

            502 -> NetworkCodeError.SERVER_NOT_REACHABLE

            204 -> NetworkCodeError.NO_AVAILABLE_DATA

            150 -> NetworkCodeError.INVALID_PARAMETER

            else -> NetworkCodeError.NO_INTERNET_CONNECTION
        }
    }
}
