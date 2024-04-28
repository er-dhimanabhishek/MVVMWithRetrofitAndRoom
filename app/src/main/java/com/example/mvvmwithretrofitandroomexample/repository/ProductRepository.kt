package com.example.mvvmwithretrofitandroomexample.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmwithretrofitandroomexample.api.ProductService
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponse
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem
import com.example.mvvmwithretrofitandroomexample.model.Response
import com.example.mvvmwithretrofitandroomexample.roomdb.ProductDatabase
import com.example.mvvmwithretrofitandroomexample.utils.NetworkUtils

class ProductRepository(private val productService: ProductService,
                        private val productDB: ProductDatabase,
                        private val context: Context) {

    private val productListLiveData = MutableLiveData<Response<List<ProductsResponseItem>>>()

    val productLiveDataObj: LiveData<Response<List<ProductsResponseItem>>>
    get() = productListLiveData

    suspend fun getProductList(){
        if (NetworkUtils.isInternetAvailable(context)) {
            try {
                productListLiveData.postValue(Response.Loading())
                val result = productService.getProductList()
                if (result.body() != null) {
                    productDB.productDao().insertProductData(result.body()?.productsResponse?.toList()!!)
                    productListLiveData.postValue(Response.Success(result.body()?.productsResponse?.toList()!!))
                }else{
                    productListLiveData.postValue(Response.Error("API error"))
                }
            }catch (e: Exception){
                productListLiveData.postValue(Response.Error(e.message.toString()))
            }
        }else{
            //hit local db
            productListLiveData.postValue(Response.Success(productDB.productDao().getProductData()))
        }
    }

}