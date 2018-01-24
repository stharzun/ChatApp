package com.arjunshrestha.chatwithme

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null

    private var mMessage: EditText? = null
    private var btnSend: Button? = null
    private var rv: RecyclerView? = null

    private val FROM = "arjunshrestha"      //from
    private val TO = "bikramkoju"           //to

    //from and to will be use to display msg

    /* private val TO = "arjunshrestha"
     private val FROM = "bikramkoju"*/

    private val UNIQUEKEY = "HamroUniqueId"  //unique id for firebase key

    private var fbd = ArrayList<FirebaseDBMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMessage = findViewById(R.id.message)
        btnSend = findViewById(R.id.send)
        rv = findViewById(R.id.mRecyclerView)
        rv?.layoutManager = LinearLayoutManager(this@MainActivity)

        mDatabase = FirebaseDatabase.getInstance().getReference()

        btnSend?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (validatingEditText()) {
                    writeToDB(mMessage?.text.toString(), TO)
                    mMessage?.setText("")
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getChatHistory()
    }

    private fun getChatHistory() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(UNIQUEKEY)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                fbd.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val data = dataSnapshot1.getValue<FirebaseDBMessage>(FirebaseDBMessage::class.java)
                    println(data)
                    fbd.add(data!!)
                }
                rv?.setHasFixedSize(true)
                rv?.setAdapter(ChatHistoryAdapter(this@MainActivity, fbd, FROM))
                rv?.scrollToPosition(fbd.size - 1)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println(databaseError)
            }
        })
    }

    private fun validatingEditText(): Boolean {
        val message = mMessage?.text.toString()
        val trimMessage = message.trim()
        if (!TextUtils.isEmpty(trimMessage))
            return true
        else
            return false
    }

    private fun writeToDB(message: String, to: String) {
        val mMessage = FirebaseDBMessage(message, to, FROM)
        mDatabase?.child(UNIQUEKEY)?.push()?.setValue(mMessage)
    }
}
