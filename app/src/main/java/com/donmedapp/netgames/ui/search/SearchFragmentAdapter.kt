package com.donmedapp.netgames.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.donmedapp.netgames.R
import com.donmedapp.netgames.Result
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.search_fragment_item.*

class SearchFragmentAdapter :
    ListAdapter<Result, SearchFragmentAdapter.ViewHolder>(GameResultDiffCallback) {

    var onItemClickListener: ((Int) -> Unit)? = null

    var currentId: Long = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.search_fragment_item, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result: Result = currentList[position]
        holder.bind(result,position)
    }


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {



        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }

        }

        fun bind(result: Result, position: Int) {
            result.run {
                currentId = id
                lblNumberS.text=position.plus(1).toString() + rating
                lblNameS.text = name
                //lblPlatforms.text = platforms!![0].platform!!.name
                imgGameS.load(backgroundImage)
            }

        }
    }

    object GameResultDiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }


    }

}