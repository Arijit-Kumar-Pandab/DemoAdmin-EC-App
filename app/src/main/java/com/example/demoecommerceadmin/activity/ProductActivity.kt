package com.example.demoecommerceadmin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demoecommerceadmin.R
import com.example.demoecommerceadmin.databinding.ActivityProductBinding

class ProductActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        setContentView(binding.root)
    }
}