package com.android.domain.domain.usecase

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PixelsToDpUseCaseTest {

    @Test
    fun `function returns the int quotient`() {
        val pixelsToDpUseCase = PixelsToDpUseCase()
        val expected = 4
        val actual = pixelsToDpUseCase.execute(12, 3f)

        Assertions.assertEquals(expected, actual)
    }
}