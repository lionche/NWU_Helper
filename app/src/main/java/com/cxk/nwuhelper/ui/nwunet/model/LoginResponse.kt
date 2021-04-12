package com.cxk.nwuhelper.ui.nwunet.model

data class LoginResponse(
    val createdAt: Int,
    val error: Int,
    val session: Session,
    val statusCode: Int,
    val token: String,
    val truncated: Boolean,
    val errorDescription: String){
    data class Session(
        val context: String,
        val id: String,
        val keepalive: Boolean,
        val keepalive_interval: Int,
        val network_changed: Boolean,
        val started_at: Int,
        val token_expires_in: Int
    )
}