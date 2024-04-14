package com.example.mvvmwithretrofitandroomexample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem
import com.example.mvvmwithretrofitandroomexample.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository):  ViewModel(){

    val productLiveDataObj: LiveData<List<ProductsResponseItem>>
        get() = productRepository.productLiveDataObj

    fun getProductList(){
        viewModelScope.launch {
            productRepository.getProductList()
        }
    }
}