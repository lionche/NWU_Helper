package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cxk.nwuhelper.ui.home.model.Post
import com.cxk.nwuhelper.ui.home.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    val myResponse : MutableLiveData<Post> = MutableLiveData()

    fun getPost(){
        viewModelScope.launch {
            val response :Post = repository.getPost()
            myResponse.value = response
        }
    }


}
