package com.example.mvvmwithretrofitandroomexample.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.mvvmwithretrofitandroomexample.Helper
import com.example.mvvmwithretrofitandroomexample.api.ProductService
import com.example.mvvmwithretrofitandroomexample.model.NetworkResult
import com.example.mvvmwithretrofitandroomexample.roomdb.ProductDatabase
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductRepositoryAPITest {

    lateinit var mockWebServer: MockWebServer
    lateinit var productService: ProductService

    @Mock
    lateinit var context:Context

    @Mock
    lateinit var productDatabase: ProductDatabase

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        mockWebServer = MockWebServer()
        productService = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ProductService::class.java)
    }

    @Test
    fun test_getProductsList_expected_emptyList() = runTest{
        val mockConnectivityManager = Mockito.mock(ConnectivityManager::class.java)
        Mockito.`when`(context.getSystemService(anyString())).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = Mockito.mock(NetworkCapabilities::class.java)
        Mockito.`when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork)).thenReturn(
            mockNetworkCapabilities)

        Mockito.`when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(
            true)
        Mockito.`when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(
            true)

        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(
            true)
        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(true)

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody("[]")

        mockWebServer.enqueue(mockResponse)

        val sut = ProductRepository(productService, productDatabase, context)
        val result = sut.getProductList()
        mockWebServer.takeRequest()

        Assert.assertEquals(true, result is NetworkResult.Success)
        Assert.assertEquals(0, result.dataList?.size)
    }

    @Test
    fun test_getProductList_expected_productList()= runTest{
        val mockConnectivityManager = Mockito.mock(ConnectivityManager::class.java)
        Mockito.`when`(context.getSystemService(anyString())).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = Mockito.mock(NetworkCapabilities::class.java)
        Mockito.`when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork)).thenReturn(
            mockNetworkCapabilities)

        Mockito.`when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(
            true)
        Mockito.`when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(
            true)

        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(
            true)
        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(
            true)

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        val content = Helper.readFileResource("/response.json")
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val sut = ProductRepository(productService, productDatabase, context)
        val result = sut.getProductList()
        mockWebServer.takeRequest()

        Assert.assertEquals(true, result is NetworkResult.Success)
        Assert.assertEquals(2, result.dataList?.size)
    }

    @Test
    fun test_getProductList_expected_error() = runTest{
        val mockConnectivityManager = Mockito.mock(ConnectivityManager::class.java)
        Mockito.`when`(context.getSystemService(anyString())).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = Mockito.mock(NetworkCapabilities::class.java)
        Mockito.`when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork)).thenReturn(
            mockNetworkCapabilities)

        Mockito.`when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(
            true)
        Mockito.`when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).thenReturn(
            true)

        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).thenReturn(
            true)
        Mockito.`when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(
            true)

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(401)
        mockResponse.setBody("Something went wrong.")
        mockWebServer.enqueue(mockResponse)

        val sut = ProductRepository(productService, productDatabase, context)
        val result = sut.getProductList()
        mockWebServer.takeRequest()

        Assert.assertEquals(true, result is NetworkResult.Error)
        Assert.assertEquals("Something went wrong.", result.errorMsg)
    }

    @After
    fun teardown(){
        mockWebServer.shutdown()
    }

}