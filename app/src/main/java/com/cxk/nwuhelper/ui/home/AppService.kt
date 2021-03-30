package com.cxk.nwuhelper.ui.home

import com.cxk.nwuhelper.ui.home.model.App
import retrofit2.Call
import retrofit2.http.GET

interface AppService {
    @GET("posts")
    fun getAppData(): Call<List<App>>
}