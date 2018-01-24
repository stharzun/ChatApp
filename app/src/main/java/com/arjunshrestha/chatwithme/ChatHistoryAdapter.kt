package com.arjunshrestha.chatwithme

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by arzun on 1/19/18.
 */
class ChatHistoryAdapter(context: Context, messageList: ArrayList<FirebaseDBMessage>, from: String) :
        RecyclerView.Adapter<ChatHistoryAdapter.MyViewHolder>() {
    internal var ctx: Context
    internal var messageList_ = ArrayList<FirebaseDBMessage>()
    internal var from_: String
    internal var inflater: LayoutInflater

    init {
        ctx = context
        messageList_ = messageList
        from_ = from
        inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        return MyViewHolder(inflater.inflate(R.layout.adapter_chat_message, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder!!.setIsRecyclable(false)
        if (from_.equals(messageList_[position].from)) {
            holder.otherLayout.visibility = View.GONE
            holder.ownMessage.text = messageList_[position].message
        } else {
            holder.ownLayout.visibility = View.GONE
            holder.otherMessage.text = messageList_[position].message
        }
    }

    override fun getItemCount(): Int {
        return messageList_.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var otherMessage: TextView
        var otherLayout: LinearLayout
        var ownMessage: TextView
        var ownLayout: LinearLayout

        init {
            otherMessage = itemView.findViewById(R.id.textViewForOthers)
            ownMessage = itemView.findViewById(R.id.textViewForOwn)
            otherLayout = itemView.findViewById(R.id.other_layout)
            ownLayout = itemView.findViewById(R.id.own_layout)
        }
    }
}