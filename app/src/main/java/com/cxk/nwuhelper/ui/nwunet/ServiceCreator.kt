package com.cxk.nwuhelper.ui.nwunet

import com.cxk.nwuhelper.BaseConstant.NWUNET_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private val retrofit = Retrofit.Builder()
        .baseUrl(NWUNET_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}