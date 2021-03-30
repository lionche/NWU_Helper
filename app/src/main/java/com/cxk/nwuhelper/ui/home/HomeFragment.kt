package com.cxk.nwuhelper.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.che.haccp.ui.base.BaseViewFragment
import com.cxk.nwuhelper.R
import com.cxk.nwuhelper.databinding.FragmentHomeBinding
import com.cxk.nwuhelper.ui.home.repository.Repository

class HomeFragment : BaseViewFragment<FragmentHomeBinding>() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.button.setOnClickListener {
        }
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show()


        val repository = Repository()
        val viewModelFactory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.getPost()
        homeViewModel.myResponse.observe(viewLifecycleOwner,  { response ->
            Log.d("test123", "onActivityCreated: ", response.title)
        })
    }

    override fun getSubLayoutId() = R.layout.fragment_home
}


