package com.cxk.nwuhelper.ui.home.repository

import com.cxk.nwuhelper.ui.home.RetrofitInstance
import com.cxk.nwuhelper.ui.home.model.Post

class Repository {
    suspend fun getPost() :Post {
        return RetrofitInstance.api.getPost()
    }
}