package com.example.mvvmwithretrofitandroomexample.api

import com.example.mvvmwithretrofitandroomexample.model.ProductsResponse
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("/products")
    suspend fun getProductList(): Response<List<ProductsResponseItem>>
}