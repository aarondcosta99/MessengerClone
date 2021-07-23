package com.example.messengerclone.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerclone.activity.ChatLogActivity
import com.example.messengerclone.R
import com.example.messengerclone.model.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class NewMessageAdapter(private val userList: ArrayList<Users>):
    RecyclerView.Adapter<NewMessageAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_row_new_message,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user: Users = userList[position]
        holder.username.text = user.username
        Picasso.get().load(user.profileUrl).into(holder.profileUrl)
        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, ChatLogActivity::class.java)
            intent.putExtra("username",user)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val username: TextView = itemView.findViewById(R.id.textView2)
        val profileUrl: CircleImageView = itemView.findViewById(R.id.exitM)
    }
}