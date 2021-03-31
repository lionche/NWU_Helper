package com.cxk.nwuhelper.ui.home.model

data class LoginPostBody(
    val deviceType: String,
    val redirectUrl: String,
    val type: String,
    val webAuthPassword: String,
    val webAuthUser: String
)