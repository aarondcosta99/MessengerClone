package com.example.messengerclone.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messengerclone.adapter.LatestAdapter
import com.example.messengerclone.databinding.ActivityLatestMessagesBinding
import com.example.messengerclone.model.Latest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LatestMessagesActivity : AppCompatActivity() {
    private lateinit var latestArrayList: ArrayList<Latest>
    private lateinit var latestAdapter: LatestAdapter
    private lateinit var bindi: ActivityLatestMessagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindi = ActivityLatestMessagesBinding.inflate(layoutInflater)
        setContentView(bindi.root)
        bindi.recylerViewLatest.layoutManager = LinearLayoutManager(this)
        bindi.recylerViewLatest.setHasFixedSize(true)
        latestArrayList = arrayListOf()
        latestAdapter = LatestAdapter(latestArrayList)
        bindi.recylerViewLatest.adapter = latestAdapter
        bindi.include.newM.setOnClickListener {
            val intent = Intent(applicationContext,NewMessageActivity::class.java)
            startActivity(intent)
        }
        bindi.include.exitM.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(applicationContext,LoginActivity::class.java))
            finish()
        }
        eventChangeListener()
    }

    private fun eventChangeListener() {
        val db = Firebase.firestore
        val uid = Firebase.auth.uid.toString()
        db.collection("latest").document("messages").collection(uid).orderBy("time",Query.Direction.DESCENDING).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            for(dc: DocumentChange in snapshot?.documentChanges!!){
                if(dc.type == DocumentChange.Type.ADDED){
                    latestArrayList.add(dc.document.toObject(Latest::class.java))
                }
            }
            latestAdapter.notifyDataSetChanged()
        }
    }

}