package com.android.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageDataModel(
    @PrimaryKey var id: String = "",
    var url: String = ""
)
