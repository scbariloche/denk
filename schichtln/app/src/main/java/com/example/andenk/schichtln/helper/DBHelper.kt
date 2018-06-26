package com.example.andenk.schichtln.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.andenk.schichtln.pojos.User
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by andreas on 29.12.2017.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, "DB", null, 1) {

    val USER_TABLE = "user"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
                "create table  if not exists " + USER_TABLE + " " +
                        "(id integer primary key, u_id integer, nickname text, username text, password text, tel text, is_staff integer, is_superuser integer)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        onCreate(db)
    }

    fun setUser(user: User): Long {

        FirebaseMessaging.getInstance().subscribeToTopic("${user.id}")
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices")


        val contentValues = ContentValues()
        contentValues.put("id", 1)
        contentValues.put("u_id", user.id)
        contentValues.put("nickname", user.username)
        contentValues.put("password", user.password)
        contentValues.put("username", user.username)
        contentValues.put("tel", user.email)
        contentValues.put("is_staff", user.is_staff)
        contentValues.put("is_superuser", user.is_superuser)
        return writableDatabase.replace(USER_TABLE, null, contentValues)
    }

    fun deleteCurrentUser(): Int {

        var id = arrayOf("1")
        FirebaseMessaging.getInstance().unsubscribeFromTopic("allDevices")
        return delete(USER_TABLE, "id", id)
    }

    fun delete(tableName: String, column: String, ids: Array<String>): Int {
        val db = this.writableDatabase
        return db.delete(
                tableName,
                column + " = ?",
                ids)
    }

    fun getCurrentUser(): User? {
        var user: User? = null
        val db = readableDatabase
        var res: Cursor? = null
        try {
            res = db.rawQuery("select * from " + USER_TABLE + " where id= 1", null)
            if (res!!.moveToFirst()) {
                user = User()
                user.id = res.getInt(res.getColumnIndex("u_id"))
                user.password = res.getString(res.getColumnIndex("password"))
                user.username = res.getString(res.getColumnIndex("nickname"))
                user.username = res.getString(res.getColumnIndex("username"))
                user.email = res.getString(res.getColumnIndex("tel"))
                user.is_staff = res.getInt(res.getColumnIndex("is_staff")) == 1
                user.is_superuser = res.getInt(res.getColumnIndex("is_superuser")) == 1
            } else {
                return null
            }

        } catch (e: Exception) {
            print(e.localizedMessage)
            return null
        } finally {
            if (res != null)
                res.close()
        }
        print(user)
        return user
    }
}