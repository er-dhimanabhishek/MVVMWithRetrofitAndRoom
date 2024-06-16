package com.example.mvvmwithretrofitandroomexample

import com.example.mvvmwithretrofitandroomexample.api.ProductService
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductAPITest {

    lateinit var mockWebServer: MockWebServer
    lateinit var productAPI: ProductService

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        productAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ProductService::class.java)
    }

    @Test
    fun test_getProducts_expected_emptyList() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody("[]")

        mockWebServer.enqueue(mockResponse)

        val response = productAPI.getProductList()
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response.isSuccessful)
        Assert.assertEquals(200, response.code())
        Assert.assertEquals(true, response.body()?.isEmpty())
    }

    @Test
    fun test_getProducts_expected_productList() = runTest{
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")

        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)

        mockWebServer.enqueue(mockResponse)

        val response = productAPI.getProductList()
        mockWebServer.takeRequest()

        Assert.assertEquals(true, response.isSuccessful)
        Assert.assertEquals(200, response.code())
        Assert.assertEquals(2, response.body()?.size)
        Assert.assertEquals("Test Product", response.body()?.get(0)?.title)
    }

    @Test
    fun test_getProducts_expected_error() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(401)
        mockResponse.setBody("Something went wrong.")

        mockWebServer.enqueue(response = mockResponse)

        val response = productAPI.getProductList()

        mockWebServer.takeRequest()

        Assert.assertEquals(401, response.code())
    }

    @After
    fun teardown(){
        mockWebServer.shutdown()
    }

}