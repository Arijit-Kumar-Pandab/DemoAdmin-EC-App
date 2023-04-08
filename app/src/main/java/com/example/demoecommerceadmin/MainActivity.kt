package com.example.demoecommerceadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.example.demoecommerceadmin.activity.AllOrderActivity
import com.example.demoecommerceadmin.activity.CategoryActivity
import com.example.demoecommerceadmin.activity.ProductActivity
import com.example.demoecommerceadmin.activity.SliderActivity
import com.example.demoecommerceadmin.databinding.ActivityMainBinding

private lateinit var binding : ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navigate()
        setContentView(binding.root)
    }
    fun navigate() {
        binding.button.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
        binding.button1.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }
        binding.button2.setOnClickListener {
            startActivity(Intent(this, SliderActivity::class.java))
        }
        binding.button3.setOnClickListener {
            startActivity(Intent(this, AllOrderActivity::class.java))
        }
    }
}