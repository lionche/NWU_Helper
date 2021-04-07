package com.cxk.nwuhelper.ui.home

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cxk.nwuhelper.MyApplication.Companion.context
import com.cxk.nwuhelper.ui.home.model.DeleteBean
import com.cxk.nwuhelper.ui.home.model.LoginPostBody
import com.cxk.nwuhelper.ui.home.model.SearchSessionsResponse

class HomeViewModel : ViewModel() {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //   检测网络

    val buttonState = MutableLiveData<String>()
    lateinit var ipAddress :String

    fun netCheck() {
//        获取 ConnectivityManager 的实例
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
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

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {

                val linkAddresses = linkProperties.linkAddresses
                val linkAddresses1 = linkAddresses.toString().indexOf("64, ")
                val linkAddresses2 = linkAddresses.toString().lastIndexOf('/')

                ipAddress = linkAddresses.toString()
                    .substring(linkAddresses1 + 4, linkAddresses2)

                val serverAddressLocation1 = linkProperties.toString().indexOf("ServerAddress")
                val serverAddressLocation2 = linkProperties.toString().indexOf("TcpBufferSizes")
                val serverAddress = linkProperties.toString()
                    .substring(serverAddressLocation1 + 16, serverAddressLocation2)


                Log.e("test123", "ip地址:$ipAddress,服务器地址:$serverAddress")

                if(ipAddress.startsWith("10") or ipAddress.startsWith("192")){
                    Toast.makeText(context, "ip地址:$ipAddress,服务器地址:$serverAddress", Toast.LENGTH_SHORT).show()
                    buttonState.postValue("wifi_available")
                }else{
                    Toast.makeText(context, "请连接校园网", Toast.LENGTH_SHORT).show()
                    buttonState.postValue("wifi_not_available")
                }

            }
        })


    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //    搜索设备


    var searchDeviceLiveData = MutableLiveData<String>()

    var deviceList = ArrayList<SearchSessionsResponse.Sessions>()

    val deviceLiveData = Transformations.switchMap(searchDeviceLiveData) { authorization ->
        Repository.searchDevices(authorization)
    }

    //登陆时赋值
    val authorization = MutableLiveData<String>()
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //    登陆设备

    var name = MutableLiveData("")
    var password = MutableLiveData("")
    val enable = MutableLiveData(false)

    private fun judgeEnable() {
        enable.value = name.value!!.isNotEmpty() && password.value!!.isNotEmpty()
        Log.d("password", "judgeEnable: ${enable.value}")
    }

    fun onPwdChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.d("password", "judgeEnable: ${enable.value}")
        password.value = s.toString()
        judgeEnable()
    }

    fun onNameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        name.value = s.toString()
        judgeEnable()
    }




    fun loginNwuStudent() {
        //根据ip修改连接
        var url =
            "http://10.16.0.12:8081/?usermac=XX:XX:XX:XX:XX:XX&userip=MYIP&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1"
        url = url.replace("MYIP", ipAddress)

        val loginPostBody = LoginPostBody(
            redirectUrl = url,
            webAuthUser = name.value!!,
            webAuthPassword = password.value!!
        )
        loginDevices(loginPostBody)
    }

    private var loginDeviceLiveData = MutableLiveData<LoginPostBody>()

    val loginLiveData = Transformations.switchMap(loginDeviceLiveData) { loginPostBody ->
        Repository.loginDevices(loginPostBody)
    }

    fun loginDevices(loginPostBody: LoginPostBody) {
        loginDeviceLiveData.value = loginPostBody
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //    删除设备


    val deviceId =
        "radius:acct:35cb8667-2649-4af3-86b7-854194219302:xx:31a6f231b5ccf8506c9454c34fb1b5af"

    private val deleteDeviceLiveData = MutableLiveData<DeleteBean>()


    val deleteLiveData = Transformations.switchMap(deleteDeviceLiveData) {
        Repository.deleteDevice(it.authorization, it.deviceId)
    }

    fun deleteDevice(authorization: String, deviceId: String) {
        val deleteBean = DeleteBean(authorization, deviceId)
        deleteDeviceLiveData.value = deleteBean
    }


}