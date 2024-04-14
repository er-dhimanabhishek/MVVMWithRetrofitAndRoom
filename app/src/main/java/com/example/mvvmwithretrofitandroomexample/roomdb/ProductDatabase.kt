package com.example.mvvmwithretrofitandroomexample.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvmwithretrofitandroomexample.model.ProductsResponseItem

@Database(entities = [ProductsResponseItem::class], version = 1, exportSchema = false)
abstract class ProductDatabase: RoomDatabase() {

    companion object{
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase{
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    ProductDatabase::class.java,
                    "ProductDB")
                    .build()
                }
            }

            return INSTANCE!!
        }
    }

    abstract fun productDao(): ProductDao

}