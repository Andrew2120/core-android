package com.b_labs.network

import com.b_labs.network.interfaces.BaseNetworkLayer
import com.b_labs.network.interfaces.NetworkProviderInterface
import com.b_labs.network.request.NetworkRequestBuilder
import com.b_labs.network.response.NetworkResponseState

class NetworkLayer constructor(networkProvider: NetworkProviderInterface) :
    BaseNetworkLayer(networkProvider) {

    override suspend fun <T, Y> callApi(
        requestData: NetworkRequestBuilder,
        successModel: Class<T>,
        failModel: Class<Y>,
    ): NetworkResponseState<T, Y> {
        // first need to call api and get response
        val responseData = getProvider().callApi(requestData)
        // after get response data whatever it success or fail will pass it to mapResponse
        return mapResponse(responseData, successModel, failModel)
    }
}
