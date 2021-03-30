package com.cxk.nwuhelper.ui.home

import com.cxk.nwuhelper.ui.home.model.Post
import retrofit2.Call
import retrofit2.http.GET

interface SimplyApi {
    @GET("posts/1")
    suspend fun getPost(): Call<Post>

}