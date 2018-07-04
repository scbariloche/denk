package com.example.andenk.schichtln.notification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId



/**
 * Created by andreas on 13.01.2018.
 */
class FirebaseHelper : FirebaseInstanceIdService() {


    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("firebasehelper", "Refreshed token: " + refreshedToken!!)

          }

}