package com.cxk.nwuhelper.ui.home.model

import com.google.gson.annotations.SerializedName

data class LoginSuccessResponse(
    val createdAt: Int,
    val error: Int,
    @SerializedName("session")
    val loginSuccessSession: LoginSuccessSession,
    val statusCode: Int,
    val token: String,
    val truncated: Boolean
)