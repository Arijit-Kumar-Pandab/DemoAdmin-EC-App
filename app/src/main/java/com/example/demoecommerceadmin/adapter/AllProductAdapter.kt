package com.example.demoecommerceadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.demoecommerceadmin.databinding.ItemProductLayoutBinding
import com.example.demoecommerceadmin.model.AllProductModel

class AllProductAdapter(val context : Context, val list : ArrayList<AllProductModel>) : Adapter<AllProductAdapter.AllProductViewHolder>() {
    inner class AllProductViewHolder(val binding : ItemProductLayoutBinding)
        : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductViewHolder {
        val binding = ItemProductLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return AllProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllProductViewHolder, position: Int) {
        val data = list[position]
        holder.binding.productName.text = data.productName.toString()
        holder.binding.productPrice.text = data.productMrp.toString()
        holder.binding.productCategory.text = data.productCategory.toString()
        Glide.with(context).load(data.productCoverImg).into(holder.binding.productImg)
    }
}