package com.example.chatapplication

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


private lateinit var binding: ActivityMainBinding
private lateinit var userRecyclerView: RecyclerView
private lateinit var userList : ArrayList<User>
private lateinit var adapter: UserAdapter
private lateinit var auth: FirebaseAuth
private lateinit var mDbRef:DatabaseReference
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth=Firebase.auth
        mDbRef=FirebaseDatabase.getInstance().getReference()
        userList= ArrayList()
        adapter = UserAdapter(this, userList)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.orange)))

        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.adapter= adapter




        mDbRef.child("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser =postSnapshot.getValue(User::class.java)

                    if (auth.currentUser!!.uid  != currentUser!!.uid){
                        userList.add(currentUser!!)
                    }else{
                        supportActionBar?.title= currentUser!!.name

                    }

                    adapter.notifyDataSetChanged()


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.logout){

            auth.signOut()
            var intent = Intent(this@MainActivity,Login::class.java)
            finish()
            startActivity(intent)

            return true
        }
        return true
    }
}