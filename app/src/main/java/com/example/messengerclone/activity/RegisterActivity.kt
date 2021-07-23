package com.example.messengerclone.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.messengerclone.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri:Uri?=null
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Firebase.auth.currentUser != null){
            startActivity(Intent(applicationContext, LatestMessagesActivity::class.java))
            finish()
        }

        binding.register.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            register()
        }

        binding.already.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(), ActivityResultCallback {
                    uri-> binding.selectphoto.setImageURI(uri)
                selectedPhotoUri = uri
            })

        binding.selectphoto.setOnClickListener {
            getAction.launch("image/*")
        }
    }

    private fun register(){

        val username = binding.username.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please enter Email and Password",Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener
                uploadImageToFireBase(username)
            }.addOnFailureListener {
                Toast.makeText(this,"Failed to Create User: ${it.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFireBase(username:String) {

        if (selectedPhotoUri == null) return

        val storage = Firebase.storage
        val storageRef = storage.reference
        val filename = UUID.randomUUID().toString()
        val ref = storageRef.child("/images/$filename")

        val uploadTask = ref.putFile(selectedPhotoUri!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d("ms","the is $downloadUri")
                val db = Firebase.firestore
                val id = Firebase.auth.uid.toString()
                val uri = downloadUri.toString()
                val user = hashMapOf(
                    "username" to username,
                    "userId" to id,
                    "profileUrl" to uri
                )
                db.collection("users").document(id).set(user).addOnSuccessListener {
                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }
}