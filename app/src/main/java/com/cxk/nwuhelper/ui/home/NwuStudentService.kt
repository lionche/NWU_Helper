package com.cxk.nwuhelper.ui.home

import com.cxk.nwuhelper.ui.home.model.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


//查询已链接的设备
interface NwuStudentService {
    //    @Headers("authorization:a9e32cf5061f67602f3745170ca37381ad22449939e7dfd905ac583a57cc79d1d67fb2a5d5218769017b566f9ca0c74a15b92f4094c57ae9")
    @GET("session/list")
    fun searchDevices(@Header("authorization") authorization: String): Call<SearchSessionsResponse>

    @Headers("content-type: application/json")
    @POST("online?noCache=1616815924226")

    fun loginDevices(
        @Header("Content-Length") contextLength: String,
        @Body loginPostBody: LoginPostBody
    ): Call<LoginSuccessResponse>


    @Headers(
        "Content-Type: application/json",
        "Content-Length: 145"
    )
    @POST("online?noCache=1616815924226")
    fun getResult(@Body loginPostBody: LoginPostBody): Call<LoginFailResponse>
}

