package com.android.domain.domain.usecase

class PixelsToDpUseCase {

    fun execute(countPixels: Int, density: Float): Int {
        val countDP = countPixels / density

        return countDP.toInt()
    }

}