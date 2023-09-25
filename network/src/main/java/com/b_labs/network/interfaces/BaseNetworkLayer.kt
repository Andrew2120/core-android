package com.b_labs.network.interfaces

import com.b_labs.network.request.NetworkRequestBuilder
import com.b_labs.network.response.NetworkResponseState
import com.b_labs.network.response.NetworkUtils
import com.b_labs.network.response.ProviderResponseData
import com.google.gson.Gson

abstract class BaseNetworkLayer constructor(private val provider: NetworkProviderInterface) {
    internal fun getProvider(): NetworkProviderInterface {
        return provider
    }

    /**
     *
     * @param requestData is the data need to call API
     * return is the model to know if response is SUCCESS with the return data or FAIL with the reason of failure
     *
     */
    abstract suspend fun <T, Y> callApi(
        requestData: NetworkRequestBuilder,
        successModel: Class<T>,
        failModel: Class<Y>,
    ): NetworkResponseState<T, Y>

    suspend fun <T> callApi(
        requestData: NetworkRequestBuilder,
        successModel: Class<T>,
    ): NetworkResponseState<T, Any> {
        return callApi(requestData, successModel, Any::class.java)
    }

    internal fun <T, Y> mapResponse(
        data: ProviderResponseData,
        successClass: Class<T>,
        failClass: Class<Y>,
    ): NetworkResponseState<T, Y> {
        return if (data.isSuccess) {
            NetworkResponseState.Success(mapToObject(data.body, successClass)!!)
        } else {
            NetworkResponseState.Fail(
                errorType = NetworkUtils.getNetworkErrorType(data.code),
                errorResponseModel = mapToObject(data.body, failClass),
                error = "",

            )
        }
    }

    private fun <T> mapToObject(responseObject: Any?, type: Class<T>): T? {
        val gson = Gson()
        return try {
            if (responseObject is String) {
                gson.fromJson(responseObject, type)
            } else {
                val json = gson.toJson(responseObject)
                gson.fromJson(json, type)
            }
        } catch (e: Throwable) {
            null
        }
    }
}
