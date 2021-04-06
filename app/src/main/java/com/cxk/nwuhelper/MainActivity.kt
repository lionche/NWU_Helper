package com.cxk.nwuhelper

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        netCheck()
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun netCheck() {
        Log.d("test123", "onCreate: Activity")
//        获取 ConnectivityManager 的实例
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
//        使用此实例获取对应用当前默认网络的引用
//        val currentNetwork = connectivityManager.activeNetwork
//        通过对网络的引用，您的应用可以查询有关网络的信息


        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                Log.e("test123", "现在的网络是$")
//            }

//            override fun onLost(network : Network) {
//                Log.e("test123", "刚刚断开网络,刚才连接的是 " + network)
//            }
//
//            override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
//                Log.e("test123", "The default network changed capabilities: " + networkCapabilities)
//            }

            override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {

                val linkAddresses = linkProperties.linkAddresses
                val ServerAddressLocation1 = linkProperties.toString().indexOf("ServerAddress")
                val ServerAddressLocation2 = linkProperties.toString().indexOf("TcpBufferSizes")
                val ServerAddress = linkProperties.toString().substring(ServerAddressLocation1+16,ServerAddressLocation2)

                Log.e("test123", "The default network changed link properties: " + linkAddresses+ServerAddress)
            }
        })


    }


}