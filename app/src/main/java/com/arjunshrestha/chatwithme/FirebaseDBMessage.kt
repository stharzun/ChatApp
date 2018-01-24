package com.arjunshrestha.chatwithme

import com.google.firebase.database.IgnoreExtraProperties


/**
 * Created by arzun on 1/19/18.
 */
@IgnoreExtraProperties
class FirebaseDBMessage {

    lateinit var message: String
    lateinit var to: String
    lateinit var from: String

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(message: String, to: String, from: String) {
        this.message = message
        this.to = to
        this.from = from
    }

}