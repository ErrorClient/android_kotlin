package com.android.ututapp.presentation.di

import com.android.data.repository.ImageRepository
import org.koin.dsl.module

val dataModule = module {
    single {
        ImageRepository.get()
    }
}