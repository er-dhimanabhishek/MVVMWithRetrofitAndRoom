package com.example.mvvmwithretrofitandroomexample.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productlist")
data class ProductsResponseItem (
    @NonNull
    @PrimaryKey()
    val id:Int,
    @ColumnInfo("image")
    val image: String? = null,
    @ColumnInfo("price")
    val price: Float? = null,
    @ColumnInfo("description")
    val description: String? = null,
    @ColumnInfo("title")
    val title: String? = null,
    @ColumnInfo("category")
    val category: String? = null,
)