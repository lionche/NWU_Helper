package com.cxk.nwuhelper.ui.home.model

data class LoginPostBody(
    val deviceType: String = "PC",
    val redirectUrl: String,
    val type:String =  "login",
    val webAuthPassword: String,
    val webAuthUser: String
)