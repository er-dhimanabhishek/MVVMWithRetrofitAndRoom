package com.example.mvvmwithretrofitandroomexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmwithretrofitandroomexample.databinding.ActivityMainBinding
import com.example.mvvmwithretrofitandroomexample.model.NetworkResult
import com.example.mvvmwithretrofitandroomexample.viewmodel.ProductViewModel
import com.example.mvvmwithretrofitandroomexample.viewmodel.ProductViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var productViewModel: ProductViewModel
    lateinit var _binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repository = (application as MyApplication).productRepository
        productViewModel = ViewModelProvider(
            this, ProductViewModelProviderFactory(repository))[ProductViewModel::class.java]

        productViewModel.getProductList()
        productViewModel.productLiveDataObj.observe(this){ res ->
            when(res){
                is NetworkResult.Loading -> {
                    showProgressBar()
                }
                is NetworkResult.Success -> {
                    res.dataList?.get(0).let { item ->
                        _binding.product = item
                    }
                    hideProgressBar()
                }
                is NetworkResult.Error -> {
                    hideProgressBar()
                    Toast.makeText(this@MainActivity, res.errorMsg, Toast.LENGTH_LONG).show()
                }
            }
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