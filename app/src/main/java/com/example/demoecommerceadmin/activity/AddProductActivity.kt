package com.example.demoecommerceadmin.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.demoecommerceadmin.R
import com.example.demoecommerceadmin.adapter.AddProductImageAdapter
import com.example.demoecommerceadmin.databinding.ActivityAddProductBinding
import com.example.demoecommerceadmin.model.AddProductModel
import com.example.demoecommerceadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var list: ArrayList<Uri>
    private lateinit var listImages: ArrayList<String>
    private lateinit var adapter: AddProductImageAdapter
    private var coverImage: Uri? = null
    private lateinit var dialog: Dialog
    private var coverImgUrl: String? = ""
    private lateinit var categoryList: ArrayList<String>

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            coverImage = it.data!!.data
            binding.productCoverImg.setImageURI(coverImage)
            binding.productCoverImg.visibility = VISIBLE
        }
    }
    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            val imageUrl = it.data!!.data
            list.add(imageUrl!!)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        list = ArrayList()
        listImages = ArrayList()
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.selectCoverImg.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.productImgBtn.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchProductActivity.launch(intent)
        }

        setProductCategory()

        adapter = AddProductImageAdapter(list)
        binding.productImgRecyclerView.adapter = adapter

        binding.submitProductBtn.setOnClickListener {
            validateData()
        }
        setContentView(binding.root)
    }

    private fun validateData() {
        if (binding.productNameEdit.text.toString().isEmpty()) {
            binding.productNameEdit.requestFocus()
            binding.productNameEdit.error = "Empty"
        } else if (binding.productSpEdit.text.toString().isEmpty()) {
            binding.productSpEdit.requestFocus()
            binding.productSpEdit.error = "Empty"
        } else if (coverImage == null) {
            Toast.makeText(this, "Please Select Cover Image", Toast.LENGTH_SHORT).show()
        } else if (list.size < 1) {
            Toast.makeText(this, "Please Select Product Images", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        dialog.show()
        val filename = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("products/$filename")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    coverImgUrl = image.toString()
                    uploadProductImage()
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private var i = 0
    private fun uploadProductImage() {
        dialog.show()
        val filename = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("products/$filename")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    listImages.add(image!!.toString())
                    if (list.size == listImages.size) {
                        storeData()
                    } else {
                        i += 1
                        uploadImage()
                    }

                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {
        val db = Firebase.firestore.collection("products")
        val key = db.document().id

        val data = AddProductModel(
            binding.productNameEdit.text.toString(),
            binding.productDescriptionEdit.text.toString(),
            coverImgUrl.toString(),
            categoryList[binding.productCategoryDropdown.selectedItemPosition],
            key,
            binding.productMrpEdit.text.toString(),
            binding.productSpEdit.text.toString(),
            listImages
        )

        db.document(key).set(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show()
            binding.productNameEdit.text = null
        } .addOnFailureListener {
            dialog.dismiss()
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setProductCategory() {
        categoryList = ArrayList()
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            categoryList.clear()
            for (doc in it.documents) {
                val data = doc.toObject(CategoryModel::class.java)
                categoryList.add(data!!.cate!!)
            }
            categoryList.add(0, "Select Category")

            val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item_layout, categoryList)
            binding.productCategoryDropdown.adapter = arrayAdapter
        }
    }
}