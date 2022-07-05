package com.android.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.android.data.models.ImageDataModel
import com.android.data.database.ImageDatabase
import com.android.data.models.StatusLoading
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "image-database"

class ImageRepository(context: Context) {

    private val database: ImageDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            ImageDatabase::class.java,
            DATABASE_NAME
        ).build()

    private val imageDao = database.imageDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val addImageToDataBase = AddImageToDataBase(imageDao = imageDao, context = context)
    private val _status = MutableStateFlow<StatusLoading>(StatusLoading.Success)
    val status = _status.asStateFlow()

    fun getAllImages(): LiveData<List<ImageDataModel>> = imageDao.getAllImages()

    fun addImage(leftToLoad: Int) {

        if (_status.value != StatusLoading.Loading) {
            executor.execute {
                runBlocking {
                    /***
                     * Старт загрузки
                     */
                    _status.value = StatusLoading.Loading

                    try {

                        for (i in 1..leftToLoad) {

                            addImageToDataBase.execute()
                        }
                        /***
                         * Загрузка успешно завершилась
                         */
                        _status.value = StatusLoading.Success

                    } catch (t: Throwable) {

                        /***
                         * Что-то пошло не так
                         */
                        _status.value = StatusLoading.Error
                    }
                }
            }
        }
    }

    companion object {

        private var INSTANCE: ImageRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = ImageRepository(context = context)
        }

        fun get(): ImageRepository {
            return INSTANCE ?: throw IllegalStateException("ImageRepository must be initialized")
        }
    }
}