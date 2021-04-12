package com.cxk.nwuhelper.ui.wenet

import com.cxk.nwuhelper.BaseConstant.WENET_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private val retrofit = Retrofit.Builder()
        .baseUrl(WENET_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}