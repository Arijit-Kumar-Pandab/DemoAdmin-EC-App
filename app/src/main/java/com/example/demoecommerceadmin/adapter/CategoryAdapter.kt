package com.example.demoecommerceadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.demoecommerceadmin.R
import com.example.demoecommerceadmin.databinding.ItemCategoryLayoutBinding
import com.example.demoecommerceadmin.model.CategoryModel

class CategoryAdapter(var context : Context, var list : java.util.ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view : View) : ViewHolder(view) {
        var binding = ItemCategoryLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.itemTextView.setText(list[position].cate.toString())
        Glide.with(context).load(list[position].img).into(holder.binding.itemImageView)
    }
}