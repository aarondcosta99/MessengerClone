package com.example.messengerclone.activity

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messengerclone.adapter.NewMessageAdapter
import com.example.messengerclone.databinding.ActivityNewMessageBinding
import com.example.messengerclone.model.Users
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class NewMessageActivity : AppCompatActivity() {

    private lateinit var userArrayList: ArrayList<Users>
    private lateinit var newMessageAdapter: NewMessageAdapter
    private lateinit var bind: ActivityNewMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        bind = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.title = "Select User"
        bind.recylerView.layoutManager = LinearLayoutManager(this)
        bind.recylerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        newMessageAdapter = NewMessageAdapter(userArrayList)
        bind.recylerView.adapter = newMessageAdapter
        eventChangeListener()
    }
    private fun eventChangeListener() {

        val db = Firebase.firestore
        db.collection("users").orderBy("username",Query.Direction.ASCENDING).addSnapshotListener { snapshot, e ->
            if (e != null)
            {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            for(dc: DocumentChange in snapshot?.documentChanges!!)
            {
                if(dc.type == DocumentChange.Type.ADDED)
                {
                    userArrayList.add(dc.document.toObject(Users::class.java))
                }
            }
            newMessageAdapter.notifyDataSetChanged()
        }
    }
}