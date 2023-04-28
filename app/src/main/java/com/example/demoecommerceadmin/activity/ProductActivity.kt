package com.example.demoecommerceadmin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demoecommerceadmin.R
import com.example.demoecommerceadmin.adapter.AllProductAdapter
import com.example.demoecommerceadmin.adapter.CategoryAdapter
import com.example.demoecommerceadmin.databinding.ActivityProductBinding
import com.example.demoecommerceadmin.model.AddProductModel
import com.example.demoecommerceadmin.model.AllProductModel
import com.example.demoecommerceadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProductActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)

        getProducts()

        Firebase.firestore.collection("products")

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        setContentView(binding.root)
    }
    private fun getProducts() {
        val list = ArrayList<AllProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(AllProductModel::class.java)
                    list.add(data!!)
                }
                binding.productRecyclerView.adapter = AllProductAdapter(this, list)
            }
    }
}