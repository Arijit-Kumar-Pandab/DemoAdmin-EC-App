package com.example.demoecommerceadmin.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.demoecommerceadmin.R
import com.example.demoecommerceadmin.databinding.ActivitySliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.URL
import java.util.*

class SliderActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySliderBinding
    private var imageUrl : Uri? = null
    private lateinit var dialog : Dialog
    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            imageUrl = it.data!!.data
            binding.image.setImageURI(imageUrl)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySliderBinding.inflate(layoutInflater)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.apply {
            image.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }

            button.setOnClickListener {
                if (imageUrl != null) {
                    uploadImage(imageUrl!!)
                } else {
                    Toast.makeText(this@SliderActivity, "Please Select Image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setContentView(binding.root)
    }

    private fun uploadImage(uri: Uri) {
        dialog.show()
        val filename = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("slider/$filename")
        refStorage.putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(image.toString())
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(image: String) {
        val db = Firebase.firestore

        val data = hashMapOf<String, Any>(
            "img" to image
        )

        db.collection("slider").document("item").set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(this, "Slider Updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}