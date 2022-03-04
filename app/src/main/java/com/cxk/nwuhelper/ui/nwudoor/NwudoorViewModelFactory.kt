package com.cxk.nwuhelper.ui.nwudoor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean

class NwudoorViewModelFactory(private val netSpBean: NetSpBean) :ViewModelProvider.Factory{
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return NwudoorViewModel(netSpBean) as T
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NwudoorViewModel(netSpBean) as T
    }
}