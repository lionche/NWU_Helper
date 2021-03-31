package com.cxk.nwuhelper.ui.home

import com.cxk.nwuhelper.ui.home.model.DeviceList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

//查询已链接的设备
interface SearchConnectDeviceService {
//    @Headers("authorization:a9e32cf5061f67602f3745170ca37381ad22449939e7dfd905ac583a57cc79d1d67fb2a5d5218769017b566f9ca0c74a15b92f4094c57ae9")
    @GET("session/list")
    fun getDeviceList(@Header("authorization") authorization:String): Call<DeviceList>
}

interface loginNwuStuedntService {
    //    @Headers("authorization:a9e32cf5061f67602f3745170ca37381ad22449939e7dfd905ac583a57cc79d1d67fb2a5d5218769017b566f9ca0c74a15b92f4094c57ae9")
    @POST("online?noCache=1616815924226")
    fun getDeviceList(@Header("Content-Length") contextLength:String): Call<DeviceList>
}