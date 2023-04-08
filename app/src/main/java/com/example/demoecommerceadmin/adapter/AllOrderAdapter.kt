package com.example.demoecommerceadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.demoecommerceadmin.databinding.AllOrderItemLayoutBinding
import com.example.demoecommerceadmin.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderAdapter(val list : ArrayList<AllOrderModel>, val context : Context) :
    Adapter<AllOrderAdapter.AllOrderViewHolder>() {
    inner class AllOrderViewHolder(val binding : AllOrderItemLayoutBinding)
        : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(
            AllOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productTitle.text = list[position].name
        holder.binding.productPrice.text = list[position].price
        holder.binding.cancelButton.setOnClickListener {
//            holder.binding.proceedButton.text = "Canceled"
            holder.binding.proceedButton.visibility = View.GONE
            updateStatus("Cancelled", list[position].orderId!!)
        }
        when(list[position].status) {
            "Ordered" -> {
                holder.binding.proceedButton.text = "Dispatched"

                holder.binding.proceedButton.setOnClickListener {
                    updateStatus("Dispatched", list[position].orderId!!)
                }
            }
            "Dispatched" -> {
                holder.binding.proceedButton.text = "Delivered"
                holder.binding.proceedButton.setOnClickListener {
                    updateStatus("Delivered", list[position].orderId!!)
                }
            }
            "Delivered" -> {
                holder.binding.cancelButton.visibility = View.GONE
                holder.binding.proceedButton.isEnabled = false
                holder.binding.proceedButton.text = "Already Delivered"
//                holder.binding.cancelButton.setOnClickListener {
//                    updateStatus("Cancelled", list[position].orderId!!)
//                }
            }
            "Cancelled" -> {
                holder.binding.proceedButton.visibility = View.GONE
                holder.binding.cancelButton.isEnabled = false
            }
        }
    }

    fun updateStatus(status : String, doc : String) {
        val data = hashMapOf<String, Any>()
        data["status"] = status
        Firebase.firestore.collection("allOrders")
            .document(doc).update(data).addOnSuccessListener {
                Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}