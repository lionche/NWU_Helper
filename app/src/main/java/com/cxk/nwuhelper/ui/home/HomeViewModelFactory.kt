package com.cxk.nwuhelper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.ui.home.model.HomeSpBean

class HomeViewModelFactory(private val homeSpBean: HomeSpBean) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(homeSpBean) as T
    }
}