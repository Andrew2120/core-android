package com.b_labs.network.interfaces

import com.b_labs.network.request.NetworkRequestBuilder
import com.b_labs.network.response.ProviderResponseData

fun interface NetworkProviderInterface {

    /**
     * @param requestData is the data need to call API
     * return is the model to know if response is SUCCESS with the return data or FAIL with the reason of failure
     */
    suspend fun callApi(requestData: NetworkRequestBuilder): ProviderResponseData
}
