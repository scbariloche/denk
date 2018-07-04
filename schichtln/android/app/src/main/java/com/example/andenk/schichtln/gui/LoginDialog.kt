package com.example.andenk.schichtln.gui

import android.app.Activity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.helper.checkPassword
import com.example.andenk.schichtln.webservice.get_user_by_name
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.dialog_login.*
import org.jetbrains.anko.longToast
import java.security.MessageDigest


class LoginDialog : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(false)
        setContentView(R.layout.dialog_login)

        txt_passwd.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {


                if (!txt_passwd.text.isEmpty() && !txt_username.text.isEmpty()) {


                    progress_bar_login.visibility = VISIBLE
                    get_user_by_name(txt_username.text.toString(),
                            on_success = {

                                if (it != null) {
                                    if (checkPassword(txt_passwd.text.toString(), it.password)) {
                                        setResult(RESULT_OK, intent)
                                        val db = DBHelper(this)
                                        db.setUser(it)
                                        FirebaseMessaging.getInstance().subscribeToTopic("${it.id}")

                                        finish()
                                    } else {
                                        longToast("wrong pw")
                                        progress_bar_login.visibility = GONE
                                    }
                                } else {
                                    longToast("404 User does not exist")

                                    progress_bar_login.visibility = GONE
                                }

                            }, on_error = {
                        longToast(it?.message.toString())
                        progress_bar_login.visibility = GONE
                    })
                } else {
                    longToast("bitte beide Felder füllen")

                }

                true
            } else {
                false
            }
        }
    }


    /**
     * Supported algorithms on Android:
     *
     * Algorithm	Supported API Levels
     * MD5          1+
     * SHA-1	    1+
     * SHA-224	    1-8,22+
     * SHA-256	    1+
     * SHA-384	    1+
     * SHA-512	    1+
     */
    private fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    override fun onBackPressed() {
//        intent.putExtra("user", User())
//        setResult(RESULT_CANCELED, intent)
//        finish()
    }

}
