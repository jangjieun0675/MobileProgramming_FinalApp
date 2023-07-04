package com.example.myfinalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.myfinalapp.databinding.ItemBoardBinding

class MyBoardViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

class MyBoardAdapter(val context: Context, val itemList: MutableList<ItemBoardModel>): RecyclerView.Adapter<MyBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBoardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyBoardViewHolder(ItemBoardBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyBoardViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            itemEmailView.text=data.email
            itemDateView.text=data.date
            itemContentView.text=data.content
        }

        //스토리지 이미지 다운로드........................
        val imageRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")
        imageRef.downloadUrl.addOnCompleteListener { task->
            if(task.isSuccessful){
                GlideApp.with(context)
                    .load(task.result)
                    .into(holder.binding.itemImageView)
            }
        }

    }
}