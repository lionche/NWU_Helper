package com.cxk.nwuhelper.ui.home.model

data class LoginResponse(
    val createdAt: Int,
    val error: Int,
    val session: LoginSuccessSession,
    val statusCode: Int,
    val token: String,
    val truncated: Boolean,
    val errorDescription: String){
    data class LoginSuccessSession(
        val context: String,
        val id: String,
        val keepalive: Boolean,
        val keepalive_interval: Int,
        val network_changed: Boolean,
        val started_at: Int,
        val token_expires_in: Int
    )
}