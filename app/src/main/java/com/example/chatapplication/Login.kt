package com.example.chatapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var intent = intent

       supportActionBar?.hide()
        auth= Firebase.auth

     supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.orange)))



        binding.signupbtn.setOnClickListener {
            var intent = Intent(this,Signup::class.java)
            startActivity(intent)
        }



        binding.loginbtn.setOnClickListener {

            var emailtxt= binding.emailtxt.text.toString()
            var passwordtxt=binding.passwordtxt.text.toString()



            auth.signInWithEmailAndPassword(emailtxt,passwordtxt).addOnSuccessListener {
                val user = auth.currentUser

                var intent = Intent(this,MainActivity::class.java)
                finish()
                startActivity(intent)

                Toast.makeText(this, "giris basarili", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }





        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}