package com.example.myfinalapp

data class ItemRetrofitModel(
    var itemName: String? = null,
    var useMethodQesitm: String? = null,
    var atpnQesitm: String? = null,
    var itemImage: String? = null
)

data class MyItem(val item:ItemRetrofitModel)
data class MyItems(val items: MutableList<MyItem>)
data class MyModel(val body: MyItems)