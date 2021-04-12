package com.cxk.nwuhelper.ui.nwunet.model

data class SearchSessionsResponse(
    val concurrency: String,
    val sessions: List<Sessions>){
    data class Sessions(
        val acct_session_id: String,
        val acct_start_time: String,
        val acct_unique_id: String,
        val assignment: String,
        val calling_station_id: String,
        val deviceType: String,
        val experienceEndTime: Int,
        val framed_ip_address: String,
        val nas_ip_address: String,
        val user_name: String
    )
}