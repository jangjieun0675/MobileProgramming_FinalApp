package com.example.myfinalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfinalapp.databinding.ItemRetrofitBinding

class MyRetrofitViewHolder(val binding: ItemRetrofitBinding): RecyclerView.ViewHolder(binding.root)

class MyRetrofitAdapter(val context: Context, val datas: MutableList<MyItem>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyRetrofitViewHolder(ItemRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyRetrofitViewHolder).binding

        //add......................................
        // null 체크 추가
        if (datas != null) {
            val model = datas[position]
            val item = model?.item // item도 null 체크

            if (item != null) {
                binding.itemName.text = item.itemName
                binding.itemCapacity.text = item.useMethodQesitm
                binding.itemRaw.text = item.atpnQesitm

                Glide.with(context)
                    .load(item.itemImage)
                    .into(binding.itemImage2)
            }
        }
    }
}