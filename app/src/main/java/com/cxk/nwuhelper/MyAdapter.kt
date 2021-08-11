package com.cxk.nwuhelper

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cxk.nwuhelper.databinding.ScoreViewHolderBinding
import com.cxk.nwuhelper.ui.nwudoor.score.model.ScoreData

class MyAdapter(private val scoresList: List<ScoreData>) :
    RecyclerView.Adapter<MyAdapter.ScoreViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val itemBinding = DataBindingUtil.inflate<ScoreViewHolderBinding>(
            LayoutInflater.from(parent.context),
            R.layout.score_view_holder,
            parent,
            false
        )
        return ScoreViewHolder(itemBinding.root, itemBinding)
    }

    override fun getItemCount() = scoresList.size


    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        Log.d("hahaha", "onBindViewHolder: "+scoresList[0].score)
        val scoresItem = scoresList[position]
        Log.d("hahaha", "onBindViewHolder: "+scoresList[position])
        holder.binding.scoreData = scoresItem
    }

    class ScoreViewHolder(itemView: View, val binding: ScoreViewHolderBinding) :
        RecyclerView.ViewHolder(itemView)
}

