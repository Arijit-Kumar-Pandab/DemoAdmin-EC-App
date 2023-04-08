package com.example.demoecommerceadmin.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.demoecommerceadmin.R
import com.example.demoecommerceadmin.adapter.CategoryAdapter
import com.example.demoecommerceadmin.databinding.ActivityCategoryBinding
import com.example.demoecommerceadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryBinding
    private var imageUrl : Uri? = null
    private lateinit var dialog : Dialog
    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            imageUrl = it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        getData()

        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }
            button6.setOnClickListener {
                validateData(binding.categoryName.text.toString())
            }
        }

        setContentView(binding.root)
    }

    private fun getData() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecycler.adapter = CategoryAdapter(this, list)
            }
    }

    private fun validateData(categoryName: String) {
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter category name", Toast.LENGTH_SHORT).show()
        } else if (imageUrl == null) {
            Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage(categoryName)
        }
    }

    private fun uploadImage(categoryName: String) {
        dialog.show()
        val filename = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("category/$filename")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(categoryName,image.toString())
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(categoryName: String, url: String) {
        val db = Firebase.firestore

        val data = hashMapOf<String, Any>(
            "cate" to categoryName,
            "img" to url
        )

        db.collection("categories").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.preview))
                binding.categoryName.text = null
                getData()
                Toast.makeText(this, "Category Addded", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}