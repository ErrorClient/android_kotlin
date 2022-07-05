package com.android.domain.domain.usecase

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any

class LeftToLoadUseCaseTest {

    @Test
    fun `returns second-first arguments if adapterSize smaller than screenSize`() {
        val leftToLoadUseCase = LeftToLoadUseCase()
        val expected = 4
        val actual =
            leftToLoadUseCase.execute(adapterSize = 2, screenSize = 6, countHorizontal = any())

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `returns missing pictures horizontally + one line if adapterSize greater or equal than screenSize`() {
        val leftToLoadUseCase = LeftToLoadUseCase()
        val expected = 6
        val actual =
            leftToLoadUseCase.execute(adapterSize = 50, screenSize = 24, countHorizontal = 4)

        Assertions.assertEquals(expected, actual)
    }
}