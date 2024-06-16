package com.example.mvvmwithretrofitandroomexample.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmwithretrofitandroomexample.getOrAwaitValue
import com.example.mvvmwithretrofitandroomexample.model.NetworkResult
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem
import com.example.mvvmwithretrofitandroomexample.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ProductViewModelTest{

    @Mock
    lateinit var productRepository: ProductRepository

    @Mock
    lateinit var context: Context

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_getProducts_expectedEmptyList() = runTest{
        Mockito.`when`(productRepository.getProductList()).thenReturn(NetworkResult.Success(
            emptyList()
        ))

        val sut = ProductViewModel(productRepository)
        sut.getProductList()

        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.productLiveDataObj.getOrAwaitValue()

        Assert.assertEquals(true, result is NetworkResult.Success)
        Assert.assertEquals(0, result.dataList?.size)

    }

    @Test
    fun test_getProducts_expectedProducts() = runTest{
        val productList = listOf<ProductsResponseItem>(
            ProductsResponseItem(1, "", 40.3F, "", "Prod 1", ""),
            ProductsResponseItem(1, "", 20.7F, "", "Prod 2", "")
        )
        Mockito.`when`(productRepository.getProductList()).thenReturn(NetworkResult.Success(
            productList))

        val sut = ProductViewModel(productRepository)
        sut.getProductList()

        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.productLiveDataObj.getOrAwaitValue()

        Assert.assertEquals(true, result is NetworkResult.Success)
        Assert.assertEquals(2, result.dataList?.size)

    }

    @Test
    fun test_getProducts_expected_error() = runTest{
        Mockito.`when`(productRepository.getProductList()).thenReturn(
            NetworkResult.Error("Something went wrong."))

        val sut = ProductViewModel(productRepository)
        sut.getProductList()

        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.productLiveDataObj.getOrAwaitValue()

        Assert.assertEquals(true, result is NetworkResult.Error)
        Assert.assertEquals("Something went wrong.", result.errorMsg)

    }

    @After
    fun teardown(){
        Dispatchers.resetMain()
    }

}