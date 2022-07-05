package com.android.ututapp.presentation.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.android.data.repository.ImageRepository
import com.android.domain.domain.usecase.LeftToLoadUseCase
import com.android.domain.domain.usecase.PixelsToDpUseCase

class ImageViewModel(
    private val imageRepository: ImageRepository,
    private val pixelsToDpUseCase: PixelsToDpUseCase,
    private val leftToLoadUseCase: LeftToLoadUseCase
) : ViewModel() {

    private var screenSize = 0
    private var _countHorizontal = 0

    val imageListLiveData = imageRepository.getAllImages()
    val status = imageRepository.status
    val countHorizontal get() = _countHorizontal

    fun addImage(adapterSize: Int) {

        val leftToLoad = leftToLoadUseCase.execute(
            adapterSize = adapterSize,
            screenSize = screenSize,
            countHorizontal = countHorizontal
        )

        imageRepository.addImage(leftToLoad = leftToLoad)
    }

    fun getSize(context: Context) {

        val displayMetrics = context.resources.displayMetrics
        val heightPixels = displayMetrics.heightPixels
        val widthPixels = displayMetrics.widthPixels
        val density = displayMetrics.density

        val countVertical = pixelsToDpUseCase.execute(heightPixels, density) / 100
        _countHorizontal = pixelsToDpUseCase.execute(widthPixels, density) / 100
        screenSize = _countHorizontal * countVertical
    }
}