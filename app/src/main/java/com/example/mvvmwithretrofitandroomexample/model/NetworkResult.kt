package com.example.mvvmwithretrofitandroomexample.model

/*sealed class NetworkResult(val dataList: List<ProductsResponseItem>?= null, val errorMsg: String? = null){
    class Loading():NetworkResult()
    class Success(dataList: List<ProductsResponseItem>):NetworkResult(dataList = dataList)
    class Error(errorMsg: String): NetworkResult(errorMsg = errorMsg)
}*/

sealed class NetworkResult<T>(val dataList: T?= null, val errorMsg: String? = null){
    class Loading<T>():NetworkResult<T>()
    class Success<T>(dataList: T? = null):NetworkResult<T>(dataList = dataList)
    class Error<T>(errorMsg: String): NetworkResult<T>(errorMsg = errorMsg)
}