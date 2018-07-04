package com.example.andenk.schichtln.helper

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.pojos.Type
import com.google.gson.GsonBuilder
import java.io.Serializable
import java.util.ArrayList

/**
 * Created by andenk on 05.12.2017.
 */

val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()



    fun createAlert(context: Context, message: String, yesAction: DialogInterface.OnClickListener, noAction: DialogInterface.OnClickListener): AlertDialog.Builder {
        return createAlert(context, message, yesAction).setNegativeButton(android.R.string.no, noAction)
    }


    fun createAlert(context: Context, message: String, yesAction: DialogInterface.OnClickListener): AlertDialog.Builder {
        return createAlert(context, message).setPositiveButton(android.R.string.yes, yesAction)
    }



    fun createAlert(
            context:Context,
            title:String,
            items:MutableList<Any>,
            onItemSelectedListener: DialogInterface.OnClickListener
    ):AlertDialog.Builder{
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(context, R.style.grade_it_alert_dialog)
        } else {
            builder = AlertDialog.Builder(context)
        }
        var names=ArrayList<String>()
        for(o in items){
            names.add(o.toString())
        }

        return builder.setTitle(title).setItems(names.toTypedArray(),onItemSelectedListener)
    }


    fun createAlert(context: Context, message: String): AlertDialog.Builder {
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(context, R.style.grade_it_alert_dialog)
        } else {
            builder = AlertDialog.Builder(context)
        }
        builder.setMessage(message)
                .setPositiveButton(android.R.string.yes) { dialog, which -> }
        return builder
    }

    fun createAlert(context: Context, message: String, yesOptionString: String, yesAction: DialogInterface.OnClickListener, noOptionString: String, noAction: DialogInterface.OnClickListener): AlertDialog.Builder {
        return createAlert(context, message).setPositiveButton(yesOptionString, yesAction).setNegativeButton(noOptionString, noAction)
    }

