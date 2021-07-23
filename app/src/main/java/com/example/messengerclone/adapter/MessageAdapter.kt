package com.example.messengerclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerclone.R
import com.example.messengerclone.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class MessageAdapter(private val chat: ArrayList<Chat>):RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private val MSGTYPELEFT = 0
    private val MSGTYPERIGHT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == MSGTYPERIGHT){
            val view1:View = LayoutInflater.from(parent.context).inflate(R.layout.chat_to_row,parent,false)
            ViewHolder(view1)
        }else{
            val view2:View = LayoutInflater.from(parent.context).inflate(R.layout.chat_from_row,parent,false)
            ViewHolder(view2)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat:Chat = chat[position]
        holder.showMessage.text = chat.message
        Picasso.get().load(chat.userPic).into(holder.userPic)
        Picasso.get().load(chat.recPic).into(holder.userPic)
    }

    override fun getItemCount(): Int {
        return chat.size
    }

    class ViewHolder(v:View) : RecyclerView.ViewHolder(v){
        val showMessage:TextView = v.findViewById(R.id.showMessage)
        val userPic:CircleImageView = v.findViewById(R.id.userPic)
        //val recPic:CircleImageView = v.findViewById(R.id.recPic)
    }

    override fun getItemViewType(position: Int): Int {
        val fuser = Firebase.auth
        return if (chat[position].userId == fuser.uid) {
            MSGTYPERIGHT
        } else {
            MSGTYPELEFT
        }
    }
}