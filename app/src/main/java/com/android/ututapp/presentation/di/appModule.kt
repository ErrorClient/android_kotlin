package com.android.ututapp.presentation.di

import com.android.ututapp.presentation.vm.ImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        ImageViewModel(
            imageRepository = get(),
            pixelsToDpUseCase = get(),
            leftToLoadUseCase = get()
        )
    }
}