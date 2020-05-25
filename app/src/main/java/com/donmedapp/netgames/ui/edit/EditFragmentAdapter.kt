package com.donmedapp.netgames.ui.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.donmedapp.netgames.R
import com.donmedapp.netgames.extensions.invisibleUnless
import com.donmedapp.netgames.utils.roundedImg
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.edit_fragment_item.*

class EditFragmentAdapter :
    ListAdapter<Int, EditFragmentAdapter.ViewHolder>(IntDiffCallback) {

    var onItemClickListener: ((Int) -> Unit)? = null

    var currentPosition: Int = -1
    //in the Constructor, pass the context in the parametres



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.edit_fragment_item, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img: Int = currentList[position]
        holder.bind(img)
    }


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(absoluteAdapterPosition) }

        }

        fun bind(img: Int) {
            imgPerfilEdit.setImageDrawable(roundedImg(img,imgPerfilEdit.context.resources))
            if (currentPosition == absoluteAdapterPosition) {
               imgPerfilEdit.setBackgroundResource(R.color.blueLight2)

                imgCheck.invisibleUnless(false)
            } else {
                imgPerfilEdit.background=null
                imgCheck.invisibleUnless(false)

            }
        }

    }

    object IntDiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem==newItem


        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem==newItem
        }

}

