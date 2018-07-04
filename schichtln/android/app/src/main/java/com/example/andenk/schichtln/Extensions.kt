package com.example.andenk.schichtln

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.andenk.schichtln.gui.LoginDialog
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.pojos.Schicht2Change
import com.example.andenk.schichtln.pojos.User
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by andreas on 24.01.2018.
 */

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun String.teaser(length: Int, linebreak: Boolean = false): String {
    if (linebreak) {
        return substring(0, length) + "\n..."
    } else {

        return substring(0, length) + "..."
    }
}

fun now(): Long {
    return System.currentTimeMillis()
}

fun line_number(): Int {
    return Throwable().stackTrace[0].lineNumber
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun Activity.openLoginDialog(resultCode: Int) {

    DBHelper(this).deleteCurrentUser()
    startActivityForResult(Intent(this, LoginDialog().javaClass), resultCode)

}

fun Activity.getDrawableByName(name: String): Drawable {
    return getDrawable(resources.getIdentifier(
            name,
            "drawable",
            packageName
    ))
}

fun User.is_same_as(user: User): Boolean {
    return (this.id == user.id
            && this.password == user.password
            && this.is_superuser == user.is_superuser
            && this.is_staff == user.is_staff
            && this.username == user.username)
}

fun Schicht.is_same_as(schicht: Schicht): Boolean {
    return (this.id == schicht.id &&
            this.day == schicht.day &&
            this.accept == schicht.accept &&
            this.offered_to == schicht.offered_to &&
            this.state == schicht.state &&
            this.type == schicht.type &&
            this.position == schicht.position
            )
}
fun Date.format(pattern:String="dd.MM."):String{
    val sdf= SimpleDateFormat(pattern)
    return sdf.format(this)
}