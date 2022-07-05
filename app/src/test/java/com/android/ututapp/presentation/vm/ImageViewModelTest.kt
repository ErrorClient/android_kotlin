package com.android.ututapp.presentation.vm

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.data.models.ImageDataModel
import com.android.data.models.StatusLoading
import com.android.data.repository.ImageRepository
import com.android.domain.domain.usecase.LeftToLoadUseCase
import com.android.domain.domain.usecase.PixelsToDpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class ImageViewModelTest {

    private val imageRepository = mock<ImageRepository>()
    private val pixelsToDpUseCase = mock<PixelsToDpUseCase>()
    private val leftToLoadUseCase = mock<LeftToLoadUseCase>()

    private lateinit var imageDataModel: ImageDataModel
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var listOfImageData: MutableList<ImageDataModel>
    private val statusLoading = StatusLoading.Success


    @BeforeEach
    fun beforeEach() {

        imageDataModel = ImageDataModel(id = "Test id", url = "Test url")
        listOfImageData = mutableListOf(imageDataModel)

        Mockito.`when`(imageRepository.getAllImages())
            .thenReturn(MutableLiveData(listOfImageData.toList()) as LiveData<List<ImageDataModel>>)

        Mockito.`when`(imageRepository.status)
            .thenReturn(MutableStateFlow(statusLoading).asStateFlow())

        imageViewModel = ImageViewModel(
            imageRepository = imageRepository,
            pixelsToDpUseCase = pixelsToDpUseCase,
            leftToLoadUseCase = leftToLoadUseCase
        )
    }

    @AfterEach
    fun afterEach() {

        Mockito.reset(imageRepository)
        Mockito.reset(pixelsToDpUseCase)
        Mockito.reset(leftToLoadUseCase)
    }

    @Test
    fun `check the correct initialization of variables`() {

        val expectedImages = listOfImageData.toList()
        val actualImages = imageViewModel.imageListLiveData.value

        Assertions.assertEquals(expectedImages, actualImages)
        Mockito.verify(
            imageRepository, Mockito.times(1)
        ).getAllImages()

        val expectedStatus = statusLoading
        val actualStatus = imageViewModel.status.value

        Assertions.assertEquals(expectedStatus, actualStatus)

        val expectedCountHorizontal = 0
        val actualCountHorizontal = imageViewModel.countHorizontal

        Assertions.assertEquals(expectedCountHorizontal, actualCountHorizontal)
    }

    @Test
    fun `check the correctness of the addImage function`() {

        val adapterSize = 5
        val screenSize = 0
        val countHorizontal = 0
        val leftToLoad = 2

        Mockito.`when`(
            leftToLoadUseCase.execute(
                adapterSize = adapterSize,
                screenSize = screenSize,
                countHorizontal = countHorizontal
            )
        )
            .thenReturn(leftToLoad)

        Mockito.`when`(imageRepository.addImage(leftToLoad = leftToLoad))
            .then {}

        imageViewModel.addImage(adapterSize)

        Mockito.verify(
            leftToLoadUseCase, Mockito.times(1)
        ).execute(
            adapterSize = adapterSize,
            screenSize = screenSize,
            countHorizontal = countHorizontal
        )

        Mockito.verify(
            imageRepository, Mockito.times(1)
        ).addImage(leftToLoad = leftToLoad)

        Mockito.verify(
            imageRepository, Mockito.times(1)
        ).addImage(leftToLoad = any())
    }

    @Test
    fun `check the correctness of the getSize function`() {

        val context = Mockito.mock(Context::class.java)
        val returnResources = Mockito.mock(Resources::class.java)
        val returnDisplayMetrics = Mockito.mock(DisplayMetrics::class.java)
            .apply {
                heightPixels = 1794
                widthPixels = 1080
                density = 2.625f
            }

        Mockito.`when`(context.resources).thenReturn(returnResources)
        Mockito.`when`(returnResources.displayMetrics).thenReturn(returnDisplayMetrics)

        Mockito.`when`(
            pixelsToDpUseCase.execute(
                returnDisplayMetrics.heightPixels,
                returnDisplayMetrics.density
            )
        ).thenReturn(683)

        Mockito.`when`(
            pixelsToDpUseCase.execute(
                returnDisplayMetrics.widthPixels,
                returnDisplayMetrics.density
            )
        ).thenReturn(411)

        imageViewModel.getSize(context)

        val expectedCountHorizontal = 4
        val actual = imageViewModel.countHorizontal

        Assertions.assertEquals(expectedCountHorizontal, actual)

        Mockito.verify(pixelsToDpUseCase, Mockito.times(2))
            .execute( any(), any() )

        Mockito.verify(pixelsToDpUseCase, Mockito.times(1))
            .execute(
                returnDisplayMetrics.heightPixels,
                returnDisplayMetrics.density
            )

        Mockito.verify(pixelsToDpUseCase, Mockito.times(1))
            .execute(
                returnDisplayMetrics.widthPixels,
                returnDisplayMetrics.density
            )

        /***
         * Дальше проверяем 2 локальные переменные
         * countVertical и screenSize
         */

        val adapterSize = 10
        val expectedScreenSize = 24

        Mockito.`when`(
            leftToLoadUseCase.execute(
                adapterSize = adapterSize,
                screenSize = expectedScreenSize,
                countHorizontal = expectedCountHorizontal
            )
        )
            .thenReturn( 0 )

        Mockito.`when`(imageRepository.addImage( leftToLoad = any() ))
            .then {}

        imageViewModel.addImage(adapterSize)

        Mockito.verify(leftToLoadUseCase, Mockito.times(1))
            .execute(
                adapterSize = adapterSize,
                screenSize = expectedScreenSize,
                countHorizontal = expectedCountHorizontal
            )
    }
}