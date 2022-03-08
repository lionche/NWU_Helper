package com.cxk.nwuhelper.ui.wenet.model

data class LoginPostBody(
    val deviceType: String = "Mushroom",
    val redirectUrl: String,
    val type:String =  "login",
    val webAuthPassword: String,
    val webAuthUser: String
)