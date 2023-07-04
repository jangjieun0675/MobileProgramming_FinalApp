package com.example.myfinalapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface NetworkService {
    // https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?
    @GET("getDrbEasyDrugList")
    fun getList(
        @Query("itemName") q:String,
        @Query("serviceKey") apikey:String,
        @Query("pageNo") page:Long,
        @Query("numOfRows") pageSize:Int,
        @Query("type") returnType:String
    ): Call<MyModel>
}