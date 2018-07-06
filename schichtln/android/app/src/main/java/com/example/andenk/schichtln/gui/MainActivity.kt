package com.example.andenk.schichtln.gui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.helper.createAlert
import com.example.andenk.schichtln.inTransaction
import com.example.andenk.schichtln.openLoginDialog
import com.example.andenk.schichtln.pojos.User
import com.example.andenk.schichtln.replaceFragment
import com.example.andenk.schichtln.webservice.get_positions_by_user
import com.example.andenk.schichtln.webservice.get_user_by_id
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast



class MainActivity : AppCompatActivity() {
    val RESULTCODE_LOGIN = 10

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.inTransaction {
                    replaceFragment(HomeBaseFragment(), R.id.main_container)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_plans -> {
                supportFragmentManager.inTransaction {
                    replaceFragment(PlanBaseFragment(), R.id.main_container)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_web -> {
                supportFragmentManager.inTransaction {
                    replaceFragment(WebViewFragment(), R.id.main_container)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
        val db = DBHelper(this)

        if (db.getCurrentUser() == null) {
            openLoginDialog(RESULTCODE_LOGIN)
        } else {

            btn_logout.text = "${db.getCurrentUser()!!.username} ausloggen"

            btn_logout.setOnClickListener({ l ->
                if (db.getCurrentUser() == null) {
                    openLoginDialog(RESULTCODE_LOGIN)
                } else {
                    createAlert(
                            this@MainActivity,
                            "${db.getCurrentUser()!!.username} wirklich abmelden?",
                            DialogInterface.OnClickListener(
                                    { d: DialogInterface,
                                      i ->
                                        btn_logout.text = "login"
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("${DBHelper(this@MainActivity).getCurrentUser()!!.id}")
                                        get_positions_by_user(
                                                DBHelper(this@MainActivity).getCurrentUser()!!,
                                                on_success = {
                                                    for (g in it) {
                                                        FirebaseMessaging.getInstance().unsubscribeFromTopic("position${g.id}")
                                                    }
                                                },
                                                on_error = {
                                                    longToast(it.localizedMessage)
                                                }
                                        )
                                        db.deleteCurrentUser()

                                        openLoginDialog(RESULTCODE_LOGIN)
                                    }
                            )
                    ).show()
                }
            }
            )
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {


        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == RESULTCODE_LOGIN) {


                onResume()
            }

        }

    }

}