package com.cxk.nwuhelper.ui.wenet

import com.cxk.nwuhelper.ui.wenet.model.LoginPostBody
import com.cxk.nwuhelper.ui.wenet.model.LoginResponse
import com.cxk.nwuhelper.ui.wenet.model.SearchSessionsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface WenetService {
    //查询已链接的设备
    @GET("session/list")
    fun searchDevices(@Header("authorization") authorization: String): Call<SearchSessionsResponse>

    //登录设备
    @Headers("Content-Type: application/json")
    @POST("online?noCache=1616815922222")
    fun loginDevice(@Body loginPostBody: LoginPostBody): Call<LoginResponse>

    //删除设备
    @DELETE("session/acctUniqueId/{deviceId}")
    fun deleteDevice(
        @Header("authorization") authorization: String,
        @Path("deviceId") deviceId: String
        ): Call<ResponseBody>
}

