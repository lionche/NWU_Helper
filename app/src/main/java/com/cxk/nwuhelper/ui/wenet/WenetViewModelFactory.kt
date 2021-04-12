package com.cxk.nwuhelper.ui.wenet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean

class WenetViewModelFactory(private val netSpBean: NetSpBean) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WenetViewModel(netSpBean) as T
    }
}