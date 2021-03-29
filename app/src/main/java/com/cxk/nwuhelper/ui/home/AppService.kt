package com.cxk.nwuhelper.ui.home

import retrofit2.Call
import retrofit2.http.GET

interface AppService {
    @GET("login")
    fun getAppData() : Call<String>

}