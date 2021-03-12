package com.che.haccp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

abstract class BaseVMFragment<T : ViewDataBinding, VM : ViewModel> : BaseViewFragment<T>() {

    protected lateinit var viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //创建viewModel
        initViewModel()
        //观察数据变化--->更新UI
        observerData()
        //设置相关事件
        initEvent()
        //开始加载数据
        startLoadData()
    }

    open fun startLoadData(){

    }

    /**
     * 设置相关事件
     */
    open fun initEvent(){

    }

    /**
     * 观察数据变化--->更新UI
     */
    open fun observerData(){

    }

    /**
     * 创建viewModel
     */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(getSubVMClass())
    }

    abstract fun getSubVMClass(): Class<VM>


}