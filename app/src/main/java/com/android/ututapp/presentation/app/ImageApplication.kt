package com.android.ututapp.presentation.app

import android.app.Application
import com.android.ututapp.BuildConfig
import com.android.data.repository.ImageRepository
import com.android.ututapp.presentation.di.appModule
import com.android.ututapp.presentation.di.dataModule
import com.android.ututapp.presentation.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

/***
 * Получаем доступ к БД на старте приложения
 */

class ImageApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ImageApplication)
            modules(listOf(appModule, domainModule, dataModule))
        }

        ImageRepository.initialize(this)
    }
}