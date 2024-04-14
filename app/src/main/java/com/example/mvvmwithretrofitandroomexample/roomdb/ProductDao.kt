package com.example.mvvmwithretrofitandroomexample.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem

@Dao
interface ProductDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertProductData(dataList: List<ProductsResponseItem>)

    @Query("SELECT * FROM productlist")
    suspend fun getProductData(): List<ProductsResponseItem>

}