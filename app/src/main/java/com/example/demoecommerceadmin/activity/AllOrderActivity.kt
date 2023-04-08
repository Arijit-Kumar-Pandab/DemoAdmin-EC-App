package com.example.demoecommerceadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demoecommerceadmin.R
import com.example.demoecommerceadmin.adapter.AllOrderAdapter
import com.example.demoecommerceadmin.databinding.ActivityAllOrderBinding
import com.example.demoecommerceadmin.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAllOrderBinding
    private lateinit var list : ArrayList<AllOrderModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()

        Firebase.firestore.collection("allOrders").get().addOnSuccessListener {
            list.clear()
            for (doc in it) {
                val data = doc.toObject(AllOrderModel::class.java)
                list.add(data)
            }

            binding.recyclerView.adapter = AllOrderAdapter(list, this)
        }

    }
}