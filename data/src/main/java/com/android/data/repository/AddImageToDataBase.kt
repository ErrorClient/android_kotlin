package com.android.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.android.data.database.ImageDao
import com.android.data.models.ImageDataModel
import com.android.data.network.RetrofitInstance

internal class AddImageToDataBase(val imageDao: ImageDao, val context: Context) {

    suspend fun execute() {
        var image = ImageDataModel()
        var existImageModel: LiveData<ImageDataModel?>? = null

        /***
         * Если в базе данных уже есть такая картинка - грузим другую
         */
        while (image.id == "" || existImageModel?.value != null) {
            image = loadImage()
            existImageModel = imageDao.getImage(image.id)
        }

        imageDao.addImage(image)
    }

    private suspend fun loadImage(): ImageDataModel {

        val isInternetAvailable = InternetAvailable(context = context)

        if (!isInternetAvailable.execute()) error("Internet is not available")

        val image = ImageDataModel()
        val responseImage = RetrofitInstance.imageSearchApi.getImage()

        if (responseImage.isSuccessful) {
            image.id = responseImage.body()?.first()?.id.toString()
            image.url = responseImage.body()?.first()?.url.toString()
        }

        return image
    }
}