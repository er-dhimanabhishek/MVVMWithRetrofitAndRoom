package com.example.mvvmwithretrofitandroomexample.model

/*sealed class Response(val dataList: List<ProductsResponseItem>?= null, val errorMsg: String? = null){
    class Loading():Response()
    class Success(dataList: List<ProductsResponseItem>):Response(dataList = dataList)
    class Error(errorMsg: String): Response(errorMsg = errorMsg)
}*/

sealed class Response<T>(val dataList: T?= null, val errorMsg: String? = null){
    class Loading<T>():Response<T>()
    class Success<T>(dataList: T? = null):Response<T>(dataList = dataList)
    class Error<T>(errorMsg: String): Response<T>(errorMsg = errorMsg)
}