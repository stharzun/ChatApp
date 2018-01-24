package com.arjunshrestha.chatwithme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private var mToolbar: Toolbar? = null
    private var mSearchAction: MenuItem? = null
    private var isSearchOpened = false
    private var edtSeach: EditText? = null

    private var mDatabase: DatabaseReference? = null

    private var mMessage: EditText? = null
    private var btnSend: Button? = null
    private var rv: RecyclerView? = null

    /*private val FROM = "arjunshrestha"      //from
    private val TO = "bikramkoju"           //to*/

    //from and to will be use to display msg

    private val TO = "arjunshrestha"
    private val FROM = "bikramkoju"

    private val UNIQUEKEY = "HamroUniqueId"  //unique id for firebase key

    private var fbd = ArrayList<FirebaseDBMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        mSearchAction = menu.findItem(R.id.action_search)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_search -> {
                handleMenuSearch()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    protected fun handleMenuSearch() {
        val action = supportActionBar //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action!!.setDisplayShowCustomEnabled(false) //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true) //show the title in the action bar

            //hides the keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edtSeach!!.getWindowToken(), 0)

            //add the search icon in the action bar
            mSearchAction?.setIcon(resources.getDrawable(R.drawable.ic_search_black_24dp))

            isSearchOpened = false
        } else { //open the search entry

            action!!.setDisplayShowCustomEnabled(true) //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar)//add the custom view
            action.setDisplayShowTitleEnabled(false) //hide the title

            edtSeach = action.getCustomView().findViewById<View>(R.id.edtSearch) as EditText //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch()
                    return@OnEditorActionListener true
                }
                false
            })


            edtSeach!!.requestFocus()

            //open the keyboard focused in the edtSearch
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT)


            //add the close icon
            mSearchAction!!.setIcon(resources.getDrawable(R.drawable.ic_close_black_24dp))

            isSearchOpened = true
        }
    }

    private fun doSearch() {
        //
    }
}
