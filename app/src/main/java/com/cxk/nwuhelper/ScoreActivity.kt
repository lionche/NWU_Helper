package com.cxk.nwuhelper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cxk.nwuhelper.databinding.ActivityScoreBinding
import com.cxk.nwuhelper.ui.nwudoor.score.adapter.ScoreAdapter
import com.cxk.nwuhelper.ui.nwudoor.score.model.ScoreData
import com.cxk.nwuhelper.utils.SerializableMap
import java.util.*


class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val bundle = intent.extras
        val myMap = bundle?.get("map") as SerializableMap
        val map = myMap.map
        val scoreList = ArrayList<ScoreData>()

//        var score = ""
        if (map != null) {
            for ((key, value) in map) {
//                score += "科目：$key 成绩：$value\n"
                Log.d("chengji", "科目：$key, 成绩：$value")
                val scoreItem = ScoreData(key,value)
                scoreList.add(scoreItem)
            }
        }
//        Toast.makeText(MyApplication.context, score, Toast.LENGTH_LONG).show()
        setScoreItemRecycler(scoreList)
    }

    private fun setScoreItemRecycler(scoresList: List<ScoreData>) {
        binding.productRecycler.adapter = ScoreAdapter( this,scoresList)
        binding.productRecycler.setHasFixedSize(true)
    }
}