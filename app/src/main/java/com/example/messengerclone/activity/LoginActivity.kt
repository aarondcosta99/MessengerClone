package com.example.messengerclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.messengerclone.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var bindin: ActivityLoginBinding
    override fun onBackPressed() {

        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        bindin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindin.root)
        bindin.login.setOnClickListener{

            bindin.progressBar2.visibility = View.VISIBLE
            val email = bindin.xyz.text.toString()
            val password = bindin.abc.text.toString()
            Firebase.auth.signInWithEmailAndPassword(email,password).addOnSuccessListener{

                val intent = Intent(applicationContext, LatestMessagesActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {

                Toast.makeText(applicationContext, "Error!!! $email", Toast.LENGTH_SHORT).show()
            }
        }
        bindin.back.setOnClickListener {

            finish()
        }
    }
}