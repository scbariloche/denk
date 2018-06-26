package com.example.andenk.schichtln.gui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.andenk.schichtln.getDrawableByName
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.is_same_as
import com.example.andenk.schichtln.openLoginDialog
import com.example.andenk.schichtln.pojos.User
import com.example.andenk.schichtln.webservice.get_user_by_id
import com.google.firebase.messaging.FirebaseMessaging

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val dra = getDrawableByName("offered")




        if (DBHelper(this).getCurrentUser() == null) {
            openLoginDialog(RESULTCODE_LOGIN)
        } else {
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            FirebaseMessaging.getInstance().subscribeToTopic("${DBHelper(this).getCurrentUser()!!.id}")
            FirebaseMessaging.getInstance().subscribeToTopic("allDevices")
            get_user_by_id(DBHelper(this).getCurrentUser()!!.id, {
                if (it != null) {
                    if (it.is_same_as(DBHelper(this).getCurrentUser()!!)) {
                        finish()
                    } else {
                        openLoginDialog(RESULTCODE_LOGIN)
                    }
                } else {
                    openLoginDialog(RESULTCODE_LOGIN)
                }
            }, {

            })


        }


    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {


        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        finish()


    }
}
