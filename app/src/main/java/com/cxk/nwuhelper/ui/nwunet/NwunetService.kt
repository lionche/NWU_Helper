package com.cxk.nwuhelper.ui.nwunet

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface NwunetService {

    //登录设备
    @FormUrlEncoded
//    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("a70.htm")
    fun loginDevice(@Field("name") name: String,@Field("password") password :String,@Field("0MKKey") MKKey :String) :Call<ResponseBody>

}

