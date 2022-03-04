package com.cxk.nwuhelper.ui.nwunet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean

class NwunetViewModelFactory(private val netSpBean: NetSpBean) :ViewModelProvider.Factory{
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return NwunetViewModel(netSpBean) as T
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NwunetViewModel(netSpBean) as T
    }
}