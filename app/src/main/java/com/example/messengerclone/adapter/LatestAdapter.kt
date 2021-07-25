package com.example.messengerclone.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerclone.R
import com.example.messengerclone.activity.ChatLogActivity
import com.example.messengerclone.model.Latest
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class LatestAdapter(private val latestList:ArrayList<Latest>) :
    RecyclerView.Adapter<LatestAdapter.LatestViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestViewHolder {

        val w = LayoutInflater.from(parent.context).inflate(R.layout.latest_message_row,parent,false)
        return LatestViewHolder(w)
    }
    override fun onBindViewHolder(holder: LatestViewHolder, position: Int) {

        val late:Latest = latestList[position]
        holder.latestMessage.text = late.message
        holder.latestUsername.text = late.username
        Picasso.get().load(late.profileUrl).into(holder.image)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context,ChatLogActivity::class.java)
            intent.putExtra("userPic",late.profileUrl)
            intent.putExtra("userId",late.userId)
            intent.putExtra("userName",late.username)
            it.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {

        return latestList.size
    }
    class LatestViewHolder(vv: View):RecyclerView.ViewHolder(vv){

        val latestUsername:TextView = vv.findViewById(R.id.latestUsername)
        val latestMessage:TextView = vv.findViewById(R.id.latestMessage)
        val image:CircleImageView = vv.findViewById(R.id.latestImage)
    }
}