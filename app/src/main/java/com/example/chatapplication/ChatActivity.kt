package com.example.chatapplication

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var mDbRef : DatabaseReference
    lateinit var mScrollView:ScrollView

    private lateinit var auth: FirebaseAuth
    var receiverRoom : String? = null
    var senderRoom : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = intent




        auth=Firebase.auth
        mDbRef = FirebaseDatabase.getInstance().getReference()
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)
        binding.chatRecyclerView.adapter=messageAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser!!.uid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title=name
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.orange)))

        mDbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)
                    messageList.add(message!!)

                           // mScrollView.fullScroll(ScrollView.FOCUS_DOWN)





                }
                messageAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })


        binding.sendbtn.setOnClickListener {


          //  mScrollView.fullScroll(ScrollView.FOCUS_DOWN)

            binding.chatRecyclerView.scrollToPosition(messageList.size+1)
            val username = auth.currentUser!!.email.toString()
            val message = binding.messageBox.text.toString()
            val messageObject = Message(username,message,senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {

                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binding.messageBox.setText("")
        }

    }

}