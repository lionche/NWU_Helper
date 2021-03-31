package com.cxk.nwuhelper.ui.home.model

data class LoginFailResponse(
    val createdAt: Int,
    val error: Int,
    val errorDescription: String,
    val statusCode: Int,
    val token: String,
    val truncated: Boolean
)