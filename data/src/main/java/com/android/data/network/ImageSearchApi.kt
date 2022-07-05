package com.android.data.network

import com.android.data.models.ImageApiModel
import retrofit2.Response
import retrofit2.http.GET

internal interface ImageSearchApi {
    @GET("/v1/images/search")
    suspend fun getImage(): Response<List<ImageApiModel>>
}