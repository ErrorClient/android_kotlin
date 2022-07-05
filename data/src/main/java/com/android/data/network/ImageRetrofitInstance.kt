package com.android.data.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val BASE_URL = "https://api.thecatapi.com"

internal object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val imageSearchApi: ImageSearchApi = retrofit.create(ImageSearchApi::class.java)
}
