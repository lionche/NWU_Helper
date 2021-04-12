package com.cxk.nwuhelper.ui.wenet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cxk.nwuhelper.ui.wenet.model.WenetSpBean

class WenetViewModelFactory(private val wenetSpBean: WenetSpBean) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WenetViewModel(wenetSpBean) as T
    }
}