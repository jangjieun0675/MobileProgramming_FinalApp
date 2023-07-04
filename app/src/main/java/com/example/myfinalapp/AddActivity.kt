package com.example.myfinalapp

import android.content.Intent
import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myfinalapp.databinding.ActivityAddBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    lateinit var filePath : String

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode === android.app.Activity.RESULT_OK){
                Glide
                    .with(applicationContext)
                    .load(it.data?.data)
                    .apply(RequestOptions().override(250,200))
                    .centerCrop()
                    .into(binding.addImageView)

                val cursor = contentResolver.query(it.data?.data as Uri, arrayOf<String>(MediaStore.Images.Media.DATA),null,null,null)
                cursor?.moveToFirst().let{
                    filePath = cursor?.getString(0) as String
                    Log.d("mobileApp","${filePath}")
                }
            }
        }

        binding.btnGallery.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            requestLauncher.launch(intent)
        }

        binding.btnSave.setOnClickListener {
            if(binding.addEditView.text.isNotEmpty() && binding.addImageView.drawable !== null){
                saveStore()
            }else{
                Toast.makeText(this,"내용을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }
    fun dateToString(date:Date):String{
        val format = SimpleDateFormat("yyyy-mm-dd hh:mm")
        return format.format(date)
    }

    fun saveStore(){
        val data = mapOf(
            "email" to MyApplication.email,
            "content" to binding.addEditView.text.toString(),
            "date" to dateToString(Date())
        )

        MyApplication.db.collection("news")
            .add(data)
            .addOnSuccessListener {
                Log.d("mobileApp","data firestore save ok")
                uploadImage(it.id)
            }
            .addOnFailureListener {
                Log.d("mobileApp","data firestore save error")
            }
    }
    fun uploadImage(docId:String){
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${docId}.jpg")
        val file = Uri.fromFile(File(filePath))
        imageRef.putFile(file)
            .addOnSuccessListener {
                Log.d("mobileApp","imageRef.putFile(file) - addOnSuccessListener")
                finish()
            }
            .addOnFailureListener {
                Log.d("mobileApp","imageRef.putFile(file) - addOnFailureListener")
            }
    }
}

