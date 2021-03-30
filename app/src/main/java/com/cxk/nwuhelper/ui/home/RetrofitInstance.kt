package com.cxk.nwuhelper.ui.home

import com.cxk.nwuhelper.BaseConstant
import com.cxk.nwuhelper.BaseConstant.REST_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(REST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api :SimplyApi by lazy {
         retrofit.create(SimplyApi::class.java)
    }

}