package com.example.chatapplication
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat.getColor
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.GroupAdapter
import com.example.chatapplication.Message
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityGroupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase





class GroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupBinding

    private lateinit var mDbRef: DatabaseReference
    private lateinit var messageAdapter:GroupAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var messageList:ArrayList<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val name = intent.getStringExtra("names")
        supportActionBar?.title="Elmalar"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.orange)))
        auth= Firebase.auth
        var username = auth.currentUser!!.email.toString()
        var currentusername = username
        mDbRef= FirebaseDatabase.getInstance().getReference()
        messageList= ArrayList()
        messageAdapter = GroupAdapter(this,messageList)
        binding.chatRecyclerView.adapter=messageAdapter
        binding.chatRecyclerView.layoutManager= LinearLayoutManager(this)


        mDbRef.child("chats").child("GrupSohbet").child("messages").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)


                    //  var msg =    message?.username.toString()



                    messageList.add(message!!)



                }
                messageAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@GroupActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })




        binding.sendbtn.setOnClickListener {

            binding.chatRecyclerView.scrollToPosition(messageList.size - 1)
            var message = binding.messageBox.text.toString()
            var messageObject = Message(message,username,"s")




            mDbRef.child("chats").child("GrupSohbet").child("messages").push()
                .setValue(Message(username,message,"s")).addOnSuccessListener {
                    // Toast.makeText(this, name, Toast.LENGTH_SHORT).show()





                }
        }








    }
}