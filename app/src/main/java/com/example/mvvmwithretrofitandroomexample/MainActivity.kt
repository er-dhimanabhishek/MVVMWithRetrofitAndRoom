package com.example.mvvmwithretrofitandroomexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmwithretrofitandroomexample.databinding.ActivityMainBinding
import com.example.mvvmwithretrofitandroomexample.viewmodel.ProductViewModel
import com.example.mvvmwithretrofitandroomexample.viewmodel.ProductViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var productViewModel: ProductViewModel
    lateinit var _binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        showProgressBar()
        val repository = (application as MyApplication).productRepository
        productViewModel = ViewModelProvider(
            this, ProductViewModelProviderFactory(repository))[ProductViewModel::class.java]

        productViewModel.getProductList()
        productViewModel.productLiveDataObj.observe(this){ productList ->
            _binding.product = productList[0]
            hideProgressBar()
        }
    }

    private fun showProgressBar() {
        _binding.tvProductItem.visibility = View.GONE
        _binding.progresBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        _binding.tvProductItem.visibility = View.VISIBLE
        _binding.progresBar.visibility = View.GONE
    }
}