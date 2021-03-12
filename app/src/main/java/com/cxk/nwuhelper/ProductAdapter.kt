package com.cxk.nwuhelper

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cxk.nwuhelper.databinding.ScoreViewHolderBinding


class ProductAdapter(var context: Context, private val productsList: List<ScoreData>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemBinding = DataBindingUtil.inflate<ScoreViewHolderBinding>(
            LayoutInflater.from(parent.context),
            R.layout.score_view_holder,
            parent,
            false
        )

        Log.d("chengji", "onCreateViewHolder: ")
        return ProductViewHolder(itemBinding.root, itemBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productItem = productsList[position]
        holder.binding.scoreData = productItem
    }

    override fun getItemCount() = productsList.size

    inner class ProductViewHolder(itemView: View, val binding: ScoreViewHolderBinding) :
        RecyclerView.ViewHolder(itemView)
}