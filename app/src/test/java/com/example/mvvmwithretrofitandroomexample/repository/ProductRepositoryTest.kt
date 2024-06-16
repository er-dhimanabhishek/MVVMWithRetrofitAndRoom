package com.example.mvvmwithretrofitandroomexample.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.Room
import com.example.mvvmwithretrofitandroomexample.api.ProductService
import com.example.mvvmwithretrofitandroomexample.model.NetworkResult
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem
import com.example.mvvmwithretrofitandroomexample.roomdb.ProductDao
import com.example.mvvmwithretrofitandroomexample.roomdb.ProductDatabase
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class ProductRepositoryTest {

    @Mock
    lateinit var productService: ProductService

    @Mock
    lateinit var productDB: ProductDatabase

    @Mock
    lateinit var context: Context

    lateinit var productDatabase: ProductDatabase
    lateinit var productDao: ProductDao


    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        productDatabase = Room.inMemoryDatabaseBuilder(
            context, ProductDatabase::class.java)
            .allowMainThreadQueries().build()
        productDao = productDatabase.productDao()
    }

    @Test
    fun test_getProducts_expectedEmptyList() = runTest{
        val mockConnectivityManager = Mockito.mock(ConnectivityManager::class.java)
        Mockito.`when`(context.getSystemService(anyString())).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = Mockito.mock(NetworkCapabilities::class.java)
        Mockito.`when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork)).thenReturn(
            mockNetworkCapabilities
        )

        Mockito.`when`(mockNetworkCapabilities?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(true)

        Mockito.`when`(mockNetworkCapabilities?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(true)

        Mockito.`when`(mockNetworkCapabilities?.hasTransport(
            NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(true)

        Mockito.`when`(mockNetworkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(true)

        Mockito.`when`(productService.getProductList()).thenReturn(Response.success(emptyList()))

        val sut = ProductRepository(productService, productDB, context)
        val result = sut.getProductList()

        assertEquals(true, result is NetworkResult.Success)
        assertEquals(0, result.dataList?.size)

    }

    @Test
    fun test_getProductList_expectedProductList() = runTest{

        val productList = listOf<ProductsResponseItem>(
            ProductsResponseItem(1, "", 40.3F, "", "Prod 1", ""),
            ProductsResponseItem(1, "", 20.7F, "", "Prod 2", "")
        )

        val mockConnectivityManager = Mockito.mock(ConnectivityManager::class.java)
        Mockito.`when`(context.getSystemService(anyString())).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = Mockito.mock(NetworkCapabilities::class.java)
        Mockito.`when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork)).thenReturn(
            mockNetworkCapabilities
        )

        Mockito.`when`(mockNetworkCapabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(true)

        Mockito.`when`(mockNetworkCapabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(true)

        Mockito.`when`(mockNetworkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(true)

        Mockito.`when`(mockNetworkCapabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(true)

        Mockito.`when`(productService.getProductList()).thenReturn(Response.success(productList))

        val sut = ProductRepository(productService, productDB, context)
        val result = sut.getProductList()

        assertEquals(true, result is NetworkResult.Success)
        assertEquals(2, result.dataList?.size)

    }

    @Test
    fun test_getProductList_expectedError() = runTest{
        val mockConnectivityManager = Mockito.mock(ConnectivityManager::class.java)
        Mockito.`when`(context.getSystemService(anyString())).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = Mockito.mock(NetworkCapabilities::class.java)
        Mockito.`when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork)).thenReturn(
            mockNetworkCapabilities
        )

        Mockito.`when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(
            true)

        Mockito.`when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(
            true)

        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(
            true)

        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(
            true)

        Mockito.`when`(productService.getProductList()).thenReturn(
            Response.error(401, "Something went wrong".toResponseBody()))

        val sut = ProductRepository(productService, productDB, context)
        val result = sut.getProductList()

        assertEquals(true, result is NetworkResult.Error)
        assertEquals("Something went wrong.", result.errorMsg)

    }

    @After
    fun teardown(){

    }
}