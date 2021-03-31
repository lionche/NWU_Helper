package com.cxk.nwuhelper.ui.home.model

import com.google.gson.annotations.SerializedName

data class SearchSessionsResponse(
    val concurrency: String,
    @SerializedName("sessions")
    val searchSessions: List<SearchSessions>
)