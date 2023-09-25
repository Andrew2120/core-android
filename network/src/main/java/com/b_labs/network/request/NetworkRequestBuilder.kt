package com.b_labs.network.request

import java.io.File

class NetworkRequestBuilder private constructor(
    val requestMethod: NetworkRequestType,
    val endPointUrl: String,
    val bodyParams: Map<String, Any>?,
    val headersParams: Map<String, String>?,
    val files: List<File>?,
    val timeoutInSeconds: Long,
    val baseUrl: String,
) {

    class Builder {
        private var requestMethod: NetworkRequestType? = null

        private var endPointUrl: String? = null

        private var timeoutInSeconds: Long? = null

        private var baseUrl: String? = null

        private var bodyParams: Map<String, Any>? = null

        private var headerParams = HashMap<String, String>()

        private var files: List<File>? = null

        fun requestMethod(requestMethod: NetworkRequestType) = apply {
            this.requestMethod = requestMethod
        }

        fun endPointUrl(endPointUrl: String) = apply {
            this.endPointUrl = endPointUrl
        }

        fun bodyParams(params: Map<String, Any>) = apply {
            this.bodyParams = params
        }

        fun headerParams(key: String, value: String) = apply {
            this.headerParams[key] = value
        }

        fun baseUrl(url: String) = apply {
            this.baseUrl = url
        }

        fun requestTimeOut(seconds: Long) = apply {
            this.timeoutInSeconds = seconds
        }

        fun files(files: List<File>) = apply {
            this.files = files
        }

        /* in this function will add all validations on request parameters */
        fun build(): NetworkRequestBuilder {
            if (requestMethod == null) {
                throw Exception("requestType not set in NetworkRequestBuilder")
            }
            if (endPointUrl == null) {
                throw Exception("endPointUrl not set in NetworkRequestBuilder")
            }
            if (timeoutInSeconds == null) {
                timeoutInSeconds = 60
            }

            return NetworkRequestBuilder(
                requestMethod = requestMethod!!,
                endPointUrl = endPointUrl!!,
                timeoutInSeconds = timeoutInSeconds!!,
                bodyParams = bodyParams,
                headersParams = headerParams,
                files = files,
                baseUrl = baseUrl!!,
            )
        }
    }
}
