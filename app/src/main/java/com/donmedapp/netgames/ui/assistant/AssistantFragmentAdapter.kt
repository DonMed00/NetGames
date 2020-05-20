package com.donmedapp.netgames.ui.assistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.donmedapp.netgames.R
import com.donmedapp.netgames.data.entity.Page
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.page_assistant_item.*
import kotlinx.android.synthetic.main.page_assistant_item.view.*

class AssistantFragmentAdapter(val pageList: ArrayList<Page>) :
    RecyclerView.Adapter<AssistantFragmentAdapter.ViewHolder>() {

    private var currentPosition :Int=0
    var onItemClickListener: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.page_assistant_item, parent, false)
        return ViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page: Page = pageList[position]
        currentPosition=position
        holder.bind(page)
    }


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            btnFinish.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }

        }

        fun bind(page: Page) {
            page.run {

                containerView.imgIcon.setImageResource(page.imageId)
                containerView.lblContent.text = page.text
                containerView.setBackgroundResource(page.colorId)
                if(adapterPosition==pageList.size-1){
                    containerView.btnFinish.visibility=View.VISIBLE
                }else{
                    containerView.btnFinish.visibility=View.INVISIBLE

                }
            }
        }
    }
}