package com.example.mvvmwithretrofitandroomexample.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmwithretrofitandroomexample.api.ProductService
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem
import com.example.mvvmwithretrofitandroomexample.roomdb.ProductDatabase
import com.example.mvvmwithretrofitandroomexample.utils.NetworkUtils

class ProductRepository(private val productService: ProductService,
                        private val productDB: ProductDatabase,
                        private val context: Context) {

    private val productListLiveData = MutableLiveData<List<ProductsResponseItem>>()

    val productLiveDataObj: LiveData<List<ProductsResponseItem>>
    get() = productListLiveData

    suspend fun getProductList(){
        if (NetworkUtils.isInternetAvailable(context)) {
            val result = productService.getProductList()
            if (result.body() != null) {
                productDB.productDao().insertProductData(result.body()?.toList()!!)
                productListLiveData.postValue(result.body()?.toList()!!)
            }
        }else{
            //hit local db
            productListLiveData.postValue(productDB.productDao().getProductData())
        }
    }

}