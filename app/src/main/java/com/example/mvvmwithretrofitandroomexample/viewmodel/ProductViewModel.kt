package com.example.mvvmwithretrofitandroomexample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem
import com.example.mvvmwithretrofitandroomexample.model.NetworkResult
import com.example.mvvmwithretrofitandroomexample.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository):  ViewModel(){

    private val productListLiveData = MutableLiveData<NetworkResult<List<ProductsResponseItem>>>()

    val productLiveDataObj: LiveData<NetworkResult<List<ProductsResponseItem>>>
        get() = productListLiveData

    fun getProductList(){
        productListLiveData.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            productListLiveData.postValue(productRepository.getProductList())
        }
    }
}