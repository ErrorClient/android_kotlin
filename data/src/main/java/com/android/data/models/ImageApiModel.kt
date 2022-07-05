package com.android.data.models

import com.squareup.moshi.Json

internal data class ImageApiModel(
    @Json(name = "id") val id: String,
    @Json(name = "url") val url: String
)