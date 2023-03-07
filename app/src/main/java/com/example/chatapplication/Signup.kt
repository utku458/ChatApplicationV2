package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapplication.databinding.ActivityLoginBinding
import com.example.chatapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth=Firebase.auth

        binding.signupbtn.setOnClickListener {

            var username = binding.nameText.text.toString()
            var emailtxt= binding.emailtxt.text.toString()
            var passwordtxt = binding.passwordtxt.text.toString()
            var user = User(username,emailtxt,auth.currentUser?.uid)
            auth.createUserWithEmailAndPassword(emailtxt,passwordtxt).addOnCompleteListener {
                if (it.isSuccessful){



                    mDbRef = FirebaseDatabase.getInstance().getReference()
                    mDbRef.child("user").child(auth.currentUser?.uid.toString()).setValue(user)





                    var intent = Intent(this,Login::class.java)
                    val user = auth.currentUser


                    finish()
                    startActivity(intent)

                }


            }


        }
    }



}