package com.donmedapp.netgames.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.donmedapp.netgames.R
import com.donmedapp.netgames.Result
import com.donmedapp.netgames.Result2
import com.donmedapp.netgames.ShortScreenshot
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.game_screen_item.*

class GameFragmentAdapter :
    ListAdapter<Result2, GameFragmentAdapter.ViewHolder>(GameResultDiffCallback) {

    var onItemClickListener: ((Int) -> Unit)? = null

    var currentId: Long = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.game_screen_item, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val screenshot = currentList[position]
        holder.bind(screenshot,position)
    }


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {



        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }

        }

        fun bind(screenshot: Result2, position: Int) {
            imgGameScreen.load(screenshot.image)
        }
    }

    object GameResultDiffCallback : DiffUtil.ItemCallback<Result2>() {
        override fun areItemsTheSame(oldItem: Result2, newItem: Result2): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result2, newItem: Result2): Boolean {
            return oldItem == newItem
        }


    }

}