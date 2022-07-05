package com.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.data.models.ImageDataModel

@Database(entities = [ImageDataModel::class], version = 1)
internal abstract class ImageDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao
}