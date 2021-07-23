package com.example.messengerclone.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.annotation.Nullable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messengerclone.adapter.MessageAdapter
import com.example.messengerclone.databinding.ActivityChatLogBinding
import com.example.messengerclone.model.Chat
import com.example.messengerclone.model.Latest
import com.example.messengerclone.model.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ChatLogActivity : AppCompatActivity() {

    private lateinit var userPic:String
    private lateinit var recPic:String
    private lateinit var recId: String
    private lateinit var mUsername:String
    private lateinit var yUsername:String
    private lateinit var bin: ActivityChatLogBinding
    private lateinit var roomId: String
    private lateinit var chatt: ArrayList<Chat>
    private lateinit var mAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(bin.root)

        val user = intent.getParcelableExtra<Users>("username")
        val name = intent.getParcelableExtra<Latest>("nameuser")

        if (user != null) {
            recId = user.userId
            recPic = user.profileUrl
            yUsername = user.username
         }
        else if (name != null)
        {
            recId = name.userId
            recPic = name.profileUrl
            yUsername = name.username
        }

        val abc = Firebase.firestore
        val userId: String? = Firebase.auth.uid
        abc.collection("users").document(userId!!).addSnapshotListener { value, _ ->
            mUsername = value?.get("username").toString()
            userPic = value?.get("profileUrl").toString()
        }

        supportActionBar?.title = yUsername

        chatt = arrayListOf()
        mAdapter = MessageAdapter(chatt)

        bin.recyclerViewChatLog.setHasFixedSize(true)
        bin.recyclerViewChatLog.layoutManager = LinearLayoutManager(this)

        when {
            userId < recId ->
            {
                roomId = userId + recId
            }
            userId.compareTo(recId) == 0 ->
            {
                Toast.makeText(this, "Error you are chatting with yourself!!!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                roomId = recId + userId
            }
        }

        readMessages(userId, recId)

        bin.buttonChatLog.setOnClickListener {

            val msg: String = bin.editTextChatLog.text.toString()

            if (msg.isNotEmpty())
            {
                performSendMessage(Firebase.auth.uid!!, recId, msg)
                bin.editTextChatLog.text.clear()
                bin.recyclerViewChatLog.scrollToPosition(mAdapter.itemCount-1)
            }
            else
            {
                Toast.makeText(this, "You can't send empty message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSendMessage(sender: String, rec: String, message: String) {

        val db = Firebase.firestore
        val uid = Firebase.auth.uid.toString()
        val time:FieldValue = FieldValue.serverTimestamp()

        val msg = hashMapOf(
            "userId" to sender,
            "recId" to rec,
            "message" to message,
            "time" to time,
            "roomId" to roomId,
            "userPic" to userPic,
            "recPic" to recPic
        )

        db.collection("chats").document(roomId).collection("messages").document().set(msg,SetOptions.merge())

        val late1 = hashMapOf(
            "userId" to recId,
            "profileUrl" to recPic,
            "username" to yUsername,
            "message" to message,
            "time" to time
        )

        db.collection("latest").document("messages").collection(uid).document(rec).set(late1)

        val late2 = hashMapOf(
            "userId" to uid,
            "profileUrl" to userPic,
            "username" to mUsername,
            "message" to message,
            "time" to time
        )

        db.collection("latest").document("messages").collection(rec).document(uid).set(late2)
    }

    private fun readMessages(userId: String, recId: String) {

        val rootRef = Firebase.firestore

        rootRef.collection("chats").document(roomId).collection("messages").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(object : EventListener<QuerySnapshot?>
        {
                override fun onEvent(@Nullable documentSnapshots: QuerySnapshot?,@Nullable e: FirebaseFirestoreException?)
                {
                    if (e != null)
                    {
                        Log.e(TAG, "onEvent: Listen failed.", e)
                        return
                    }
                    chatt.clear()
                    if (documentSnapshots != null)
                    {
                        for (queryDocumentSnapshots in documentSnapshots)
                        {
                            val chat = queryDocumentSnapshots.toObject(Chat::class.java)
                            if (chat.recId == recId && chat.userId == userId || chat.recId == userId && chat.userId == recId)
                            {
                                chatt.add(chat)
                            }
                            mAdapter = MessageAdapter(chatt)
                            bin.recyclerViewChatLog.adapter = mAdapter
                        }
                    }
                }
            })
    }
}