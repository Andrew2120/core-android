package com.b_labs.network.provider.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Retrofit APIs Handles the different methods supported by the retrofit
 */
internal interface RetrofitAPIs {

    @POST
    @JvmSuppressWildcards
    suspend fun requestPOSTMethod(
        @Url endPointUrl: String,
        @HeaderMap headers: Map<String, Any>,
        @Body params: Map<String, Any>,
    ): Response<Any>

    @POST
    @Multipart
    @JvmSuppressWildcards
    suspend fun requestPOSTWithMultiPartMethod(
        @Url endPointUrl: String,
        @PartMap params: Map<String, RequestBody>,
        @Part files: List<MultipartBody.Part>,
        @HeaderMap headers: Map<String, Any> = mapOf(),
    ): Response<Any>

    @GET
    @JvmSuppressWildcards
    suspend fun requestGETMethod(
        @Url endPointUrl: String,
        @QueryMap(encoded = true) params: Map<String, Any>,
        @HeaderMap headers: Map<String, Any>,
    ): Response<Any>

    @JvmSuppressWildcards
    @HTTP(method = "PUT", hasBody = true)
    suspend fun requestPUTMethod(
        @Url endPointUrl: String,
        @Body params: Map<String, Any>,
        @HeaderMap headers: Map<String, Any>,
    ): Response<Any>

    @JvmSuppressWildcards
    @HTTP(method = "DELETE", hasBody = true)
    suspend fun requestDELETEMethod(
        @Url endPointUrl: String,
        @Body params: Map<String, Any>,
        @HeaderMap headers: Map<String, String>,
    ): Response<Any>
}
