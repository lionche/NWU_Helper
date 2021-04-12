package com.cxk.nwuhelper.ui.nwunet

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface NwunetService {

    //登录设备
    @FormUrlEncoded
    @POST("a70.htm")
    fun loginDevice(
        @Field("DDDDD") name: String,
        @Field("upass") password: String,
        @Field("0MKKey") MKKey: String
    ): Call<ResponseBody>

}

