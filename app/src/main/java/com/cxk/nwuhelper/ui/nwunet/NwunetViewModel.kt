package com.cxk.nwuhelper.ui.nwunet


import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.NetworkUtils
import com.cxk.nwuhelper.MyApplication.Companion.context
import com.cxk.nwuhelper.ui.wenet.model.LoginPostBody
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean


class NwunetViewModel(netSpBean: NetSpBean) : ViewModel() {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //   检测网络

    val buttonState = MutableLiveData<String>()

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


//                val linkAddresses = linkProperties.linkAddresses
//                val linkAddresses1 = linkAddresses.toString().indexOf("64, ")
//                val linkAddresses2 = linkAddresses.toString().lastIndexOf('/')
//
//                ipAddress = linkAddresses.toString()
//                    .substring(linkAddresses1 + 4, linkAddresses2)
//
//                val serverAddressLocation1 = linkProperties.toString().indexOf("ServerAddress")
//                val serverAddressLocation2 = linkProperties.toString().indexOf("TcpBufferSizes")
//                val serverAddress = linkProperties.toString()
//                    .substring(serverAddressLocation1 + 16, serverAddressLocation2)
//                serverAddress.showToast(context)

//                Log.e("test123", "ip地址:$ipAddress,服务器地址:$serverAddress")
//                Log.e("test123", "$linkProperties")\
                val ServerAddressByWifi = NetworkUtils.getServerAddressByWifi()
                val isWifiAvailable = NetworkUtils.isWifiAvailable()
//                ("ip:${IpAddressByWifi},server:$ServerAddressByWifi").showToast(context)
                Log.d("test123", "ServerAddressByWifi,$ServerAddressByWifi")
                Log.d("test123", "isWifiAvailable,$isWifiAvailable")


                if (!isWifiAvailable) {
//                    "请打开Wi-Fi".showToast(context)
                    buttonState.postValue("wifi_not_available")
                } else if ("192.168" in ServerAddressByWifi) {
//                    "暂时不支持路由器".showToast(context)
                    buttonState.postValue("wifi_not_available")
                } else if ("10.17.254.254" in ServerAddressByWifi) {
//                    "可以登陆".showToast(context)
                    buttonState.postValue("wifi_not_available")

                } else if ("172.18.6.6" in ServerAddressByWifi) {
//                    "暂时不支持NWUNET".showToast(context)
                    buttonState.postValue("wifi_available")
                } else {
//                    "流量".showToast(context)
                    buttonState.postValue("wifi_not_available")
                }
            }
        })


    }



    //登陆时赋值
    val authorization = MutableLiveData<String>()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    登陆设备


    var name: String
    var password: String
    val autoLoginLiveData = MutableLiveData<Boolean>()
    val rmPasswordLiveData = MutableLiveData<Boolean>()

    init {
        name = netSpBean.name
        password = netSpBean.password
        autoLoginLiveData.value = netSpBean.autoLogin
        rmPasswordLiveData.value = netSpBean.rmPassword
    }

    var netAvailable = false

    val enable = MutableLiveData(false)

    fun judgeEnable() {
        enable.postValue(name.isNotEmpty() && password.isNotEmpty() && netAvailable)

        Log.d("password", "judgeEnable: ${enable.value}")
    }

    fun onPwdChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        password = s.toString()
        judgeEnable()
    }

    fun onNameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        name = s.toString()
        judgeEnable()
    }


    /**
     * 登陆wenet校园网
     */
    fun loginWenet() {

        val loginPostBody = LoginPostBody(
            redirectUrl = "213",
            webAuthUser = name,
            webAuthPassword = password
        )
        loginDevices(loginPostBody)
        buttonState.value = "start_to_login"

    }

    private var loginDeviceLiveData = MutableLiveData<LoginPostBody>()

    val loginLiveData = Transformations.switchMap(loginDeviceLiveData) {  Repository.loginDevices()   }

    fun loginDevices(loginPostBody: LoginPostBody) {
        loginDeviceLiveData.value = loginPostBody
    }


    /**
     * 登陆nwunet校园网
     */

    fun loginNwunet(){
        val rawBody = "DDDDD=MYNAME&upass=MYPASSWORD&R1=0&R2=&R3=0&R6=0&para=00&0MKKey=123456&v6ip=&hid1=5256&hid2=14250008&cn=0&buttonClicked=&redirect_url=&err_flag=&username=&password=&user=&cmd=&Login="
        rawBody.apply {
            replace("MYNAME", name)
            replace("MYPASSWORD", password)
        }

    }
}