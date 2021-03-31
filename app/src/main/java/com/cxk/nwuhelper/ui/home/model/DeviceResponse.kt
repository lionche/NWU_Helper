package com.cxk.nwuhelper.ui.home.model

data class DeviceResponse(
    val concurrency: String,
    val sessions: List<Session>
)