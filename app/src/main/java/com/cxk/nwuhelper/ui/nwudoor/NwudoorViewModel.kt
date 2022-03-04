package com.cxk.nwuhelper.ui.nwudoor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cxk.nwuhelper.BaseConstant.NWU_LOGIN_URL
import com.cxk.nwuhelper.BaseConstant.SCORE_URL
import com.cxk.nwuhelper.MyApplication
import com.cxk.nwuhelper.ui.nwudoor.score.model.ScoreData
import com.cxk.nwuhelper.ui.nwudoor.score.util.unzip.unzipPdf
import com.cxk.nwuhelper.ui.wenet.model.NetSpBean
import com.cxk.nwuhelper.utils.encrypt
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set


class NwudoorViewModel(netSpBean: NetSpBean) : ViewModel() {

    var name: String
    var password: String
    val autoLoginLiveData = MutableLiveData<Boolean>()
    val rmPasswordLiveData = MutableLiveData<Boolean>()

    val enable = MutableLiveData(false)

    init {
        name = netSpBean.name
        password = netSpBean.password
        autoLoginLiveData.value = netSpBean.autoLogin
        rmPasswordLiveData.value = netSpBean.rmPassword
    }


    fun judgeEnable() {
        enable.value = name.isNotEmpty() && password.isNotEmpty()
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


    fun visitWebsiteNewThread() {

        object : Thread() {
            override fun run() {
                visitWebsite()
            }
        }.start()
        buttonState.postValue("start_to_login")

    }
    //定义cookies


    fun visitWebsite() {
        Log.d("chengji", "开始查询")

        val connect: Connection = Jsoup.connect(NWU_LOGIN_URL)
//        connect.header(USER_AGENT, USER_AGENT_VALUE) // 配置模拟浏览器
        val response: Connection.Response = connect.execute()
        val cookies: Map<String, String> = response.cookies()
//        cookieLiveData.postValue(cookies)
        //        System.out.println("cookies:" + cookies);
        val document = Jsoup.parse(response.body())
        Log.d("website1", document.toString())
        val stringStringMap: Map<String, String> = extractInfo(document)
        loginWebsite(cookies, stringStringMap)
    }


    companion object { // 注解---1
        var cookies1: Map<String, String>? = null
    }

    //登陆需要的cookie
    private fun loginWebsite(
        cookies: Map<String, String>,
        stringStringMap: Map<String, String>
    ) {
        val lt = stringStringMap["lt"]
        val execution = stringStringMap["execution"]
        val passwordAfterEncrypt = stringStringMap["password"]
        val connect = Jsoup.connect(NWU_LOGIN_URL).cookies(cookies).followRedirects(false)
        connect.data("username", name)
        connect.data("password", passwordAfterEncrypt)
        connect.data("lt", lt)
        connect.data("dllt", "userNamePasswordLogin")
        connect.data("execution", execution)
        connect.data("_eventId", "submit")
        connect.data("rmShown", "1")
        connect.method(Connection.Method.POST)
        val response = connect.execute()

        cookies1 = response.cookies()

        //登陆所需要的cookie,放入livedata




        val document = Jsoup.parse(response.body()).toString()
        Log.d("website2", document)

        //登陆成功
        if ("This document you requested has moved temporarily" in document) {

//            buttonState.postValue("login_success")

//            searchScore(cookies1)

            //下载pdf
            downloadScorePdf(name,cookies1!!)
            //解压pdf
            unzipPdf(
                File(MyApplication.context.filesDir, "temp.zip"),
                MyApplication.context.filesDir.absolutePath
            )
            File(MyApplication.context.filesDir, "temp.zip").delete()
            Log.d("downoadpdf", "下载${name}成功")

            buttonState.postValue("login_success")


        } else {
            buttonState.postValue("wrong_password")
        }


    }


    fun Download2showPdf(name: String,cookies: Map<String, String>) {

        object : Thread() {
            override fun run() {
                downloadScorePdf(name,cookies)
                //解压pdf
                unzipPdf(
                    File(MyApplication.context.filesDir, "temp.zip"),
                    MyApplication.context.filesDir.absolutePath
                )
                File(MyApplication.context.filesDir, "temp.zip").delete()
                Log.d("downoadpdf", "下载${name}成功")
                buttonState.postValue("load_pdf")



            }
        }.start()


    }


    private fun downloadScorePdf(name: String,cookies: Map<String, String>) {
        Log.d("downoadpdf", "正在下载${name}")

        var url =
            "http://yjsxt.nwu.edu.cn/py/page/student/cjgrcx.htm?pageAction=download&xh=MYNUMBER"
        url = url.replace("MYNUMBER", name)

        //下载网址的url地址
        val response =
            Jsoup.connect(url)
                .cookies(cookies)
                .followRedirects(true)
                .timeout(10000)
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute()

        val out = FileOutputStream(File(MyApplication.context.filesDir, "temp.zip"))
        out.write(response.bodyAsBytes())
        out.close()

    }


    private fun extractInfo(document: Document): Map<String, String> {
        val aesKey: String =
            document.getElementsByTag("head").first().getElementsByTag("script").eq(1).toString()
                .split("\"")[5]
        //        System.out.println("aes key:" + aes_key);
        val lt: String = document.getElementsByTag("input").eq(4).`val`()
        //        System.out.println("lt:" + lt);
        val execution: String = document.getElementsByTag("input").eq(6).`val`()
        //        System.out.println("execution:" + execution);
        val passwordAfterEncrypt = encrypt.Encrypt(password, aesKey)
        //        System.out.println("passwordAfterEncrypt:" + passwordAfterEncrypt);
        val map: MutableMap<String, String> = HashMap()
        map["password"] = passwordAfterEncrypt
        map["lt"] = lt
        map["execution"] = execution
        return map
    }

    //查询成绩
    fun searchScore(cookies: MutableMap<String, String>) {
        Log.d("website3", "访问website3")

        val connect =
            Jsoup.connect(SCORE_URL).cookies(cookies).followRedirects(true).timeout(10000)
        connect.method(Connection.Method.GET)
        val response = connect.execute()
        val document = Jsoup.parse(response.body())
        Log.d("website3", document.toString())
        val elementsScore = document.getElementsByTag("tbody").first().getElementsByTag("tr")
        Log.d("elementsScorevm", "searchScroe: ${elementsScore}")
        giveScore(elementsScore)
    }

    //晨午检
//    fun report(cookies:MutableMap<String, String>){
//        Log.d("chenwujian", "访问晨午检")
//        val connect =
//            Jsoup.connect(REPORT_URL).cookies(cookies).followRedirects(true).timeout(10000)
//        connect.method(Connection.Method.GET)
//        val response = connect.execute()
//        val document = Jsoup.parse(response.body())
//        Log.d("chenwujian", document.toString())
//
//    }


    //    val scoreMap: MutableMap<String, String> = HashMap()
    val scoreListLiveData = MutableLiveData<ArrayList<ScoreData>>()

    private fun giveScore(elementsScore: Elements) {
//        Log.d("website3detail", elements.toString())

        val scoreList = ArrayList<ScoreData>()

        //        System.out.println(elements.first().getElementsByTag("td").get(0).text());
        for (value in elementsScore) {
//            Log.d("website3detail", value.toString()+"---")

            val subject: String = value.getElementsByTag("td")[3].text()
            val score: String = value.getElementsByTag("td")[4].text()
            Log.d("website3detail", "$subject,$score")

//            scoreMap.put(subject, score)
//            scoreMap[subject] = score

            val scoreItem = ScoreData(subject, score)
            scoreList.add(scoreItem)
        }
        //将查询到的成绩存入scoreListLiveData
        scoreListLiveData.postValue(scoreList)

//        resultMap.postValue(scoreMap)


    }

    val buttonState = MutableLiveData<String>()

    val scoreFragmentState = MutableLiveData<String>()


}
