package com.example.mvvmwithretrofitandroomexample.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmwithretrofitandroomexample.api.ProductService
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem
import com.example.mvvmwithretrofitandroomexample.model.NetworkResult
import com.example.mvvmwithretrofitandroomexample.roomdb.ProductDatabase
import com.example.mvvmwithretrofitandroomexample.utils.NetworkUtils

class ProductRepository(private val productService: ProductService,
                        private val productDB: ProductDatabase,
                        private val context: Context) {

    suspend fun getProductList(): NetworkResult<List<ProductsResponseItem>>{
        return if (NetworkUtils.isInternetAvailable(context)) {
                val result = productService.getProductList()
                if (result.isSuccessful) {
                    if (result.body() != null) {
                        //productDB.productDao().insertProductData(result.body()?.toList()!!)
                        NetworkResult.Success(result.body()?.toList()!!)
                    } else {
                        NetworkResult.Error("API error")
                    }
            }else{
                NetworkResult.Error("Something went wrong.")
            }
        }else{
            //hit local db
            //NetworkResult.Success(productDB.productDao().getProductData())
            NetworkResult.Error("No internet.")
        }
    }

}