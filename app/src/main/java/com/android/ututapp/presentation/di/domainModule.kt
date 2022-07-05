package com.android.ututapp.presentation.di

import com.android.domain.domain.usecase.LeftToLoadUseCase
import com.android.domain.domain.usecase.PixelsToDpUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
        PixelsToDpUseCase()
    }

    factory {
        LeftToLoadUseCase()
    }
}