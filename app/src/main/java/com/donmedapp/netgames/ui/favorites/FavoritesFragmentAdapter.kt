package com.donmedapp.netgames.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.donmedapp.netgames.R
import com.donmedapp.netgames.data.pojo.Game
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.favorites_fragment_item.*

class FavoritesFragmentAdapter :
    ListAdapter<Game, FavoritesFragmentAdapter.ViewHolder>(GameResultDiffCallback) {

    var onItemClickListener: ((Int) -> Unit)? = null

    var currentPosition: Int = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.favorites_fragment_item, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result: Game = currentList[position]
        holder.bind(result,position)
    }


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {



        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }

        }

        fun bind(result: Game, position: Int) {
            result.run {

                lblNumberF.text=position.toString()
                lblNameF.text = name
                lblReleased.text=released
                imgGameF.load(backgroundImg)
            }

        }
    }

    object GameResultDiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.gameId == newItem.gameId
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }


    }

}