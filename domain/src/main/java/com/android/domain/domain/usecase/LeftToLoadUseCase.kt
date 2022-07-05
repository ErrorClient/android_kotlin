package com.android.domain.domain.usecase

class LeftToLoadUseCase {

    fun execute(adapterSize: Int, screenSize: Int, countHorizontal: Int): Int {

        return if (adapterSize < screenSize) {

            screenSize - adapterSize
        } else {

            val quotientDouble = adapterSize / countHorizontal.toDouble()
            val quotientInt = adapterSize / countHorizontal
            val fractionPart =
                1 - ( quotientDouble - quotientInt )

            val leftToLoadHorizontal = fractionPart * countHorizontal

            ( leftToLoadHorizontal + countHorizontal ).toInt()
        }
    }
}