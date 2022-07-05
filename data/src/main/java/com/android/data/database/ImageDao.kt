package com.android.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.data.models.ImageDataModel

@Dao
internal interface ImageDao {

    @Query("SELECT * FROM imageDataModel")
    fun getAllImages(): LiveData<List<ImageDataModel>>

    @Query("SELECT * FROM imageDataModel WHERE id=(:id)")
    fun getImage(id: String): LiveData<ImageDataModel?>

    @Insert
    fun addImage(imageModel: ImageDataModel)
}