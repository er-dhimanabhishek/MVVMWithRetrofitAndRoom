package com.example.mvvmwithretrofitandroomexample

import android.app.Application
import com.example.mvvmwithretrofitandroomexample.api.ProductService
import com.example.mvvmwithretrofitandroomexample.api.RetrofitHelper
import com.example.mvvmwithretrofitandroomexample.repository.ProductRepository
import com.example.mvvmwithretrofitandroomexample.roomdb.ProductDatabase

class MyApplication: Application() {

    lateinit var productRepository: ProductRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val productService = RetrofitHelper.getApiClient().create(ProductService::class.java)
        val dataBase = ProductDatabase.getInstance(applicationContext)
        productRepository = ProductRepository(productService, dataBase)
    }

}