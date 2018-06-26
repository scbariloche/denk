package com.example.andenk.schichtln.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.gui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FireBaseMessageHandler :  FirebaseMessagingService(), assambleNotification {
    var notificationManager: NotificationManager? = null
    var smallIconId = R.drawable.ic_theaters_black_48dp
    var title = ""
    var body = "Notification"
    var autocancel: Boolean = true
    var lightColor: String = "#600001"
    var lightOn: Int = 300
    var lightOff: Int = 300
    var vibratePattern: LongArray = longArrayOf(0, 200, 50, 300)
    var pendingIntent: PendingIntent? = null
    var intent: Intent? = null
    var ongoing: Boolean = false
    var ticker: String = ""

    var channelid: Long = 0



    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        channelid = System.currentTimeMillis()
        val data = remoteMessage!!.data
        Log.i("notification", "Messagingservice onMessageReceived")
        title = resources.getString(R.string.app_name)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        checkContent(remoteMessage)
        onNotificationReady()
    }



    private fun checkContent(remoteMessage: RemoteMessage) {


        if (remoteMessage.data["title"] != null) {
            title = remoteMessage.data["title"]!!
        }
        if (remoteMessage.data["body"] != null) {
            body = remoteMessage.data["body"]!!
        }
        if (remoteMessage.data["autocancel"] != null) {
            autocancel = java.lang.Boolean.parseBoolean(remoteMessage.data["autocancel"])
        }
        if (remoteMessage.data["lightColor"] != null) {
            lightColor = remoteMessage.data["lightColor"]!!
        }
        if (remoteMessage.data["lightOn"] != null) {
            lightOn = Integer.parseInt(remoteMessage.data["lightOn"])
        }
        if (remoteMessage.data["lightOff"] != null) {
            lightOff = Integer.parseInt(remoteMessage.data["lightOff"])
        }
        if (remoteMessage.data["vibratePattern"] != null) {
            val vibrateString = remoteMessage.data["vibratePattern"]!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            vibratePattern = LongArray(vibrateString.size)
            for (i in vibrateString.indices) {
                vibratePattern[i] = java.lang.Long.parseLong(vibrateString[i])
            }
        }
        if (remoteMessage.data["ongoing"] != null) {
            ongoing = java.lang.Boolean.parseBoolean(remoteMessage.data["ongoing"])
        }
        if (remoteMessage.data["ticker"] != null) {
            ticker = remoteMessage.data["ticker"]!!
        }
        val intent = Intent(
                Intent(applicationContext, MainActivity::class.java))

        pendingIntent = PendingIntent.getActivity(
                this,
                10,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    fun sendNotification(
            smallIconId: Int,
            title: String,
            body: String,
            autocancel: Boolean,
            lightColor: String,
            lightOn: Int,
            lightOff: Int,
            vibratePattern: LongArray,
            pendingIntent: PendingIntent?,
            ongoing: Boolean,
            ticker: String
    ) {
        val channel: NotificationChannel

        val mBuilder = NotificationCompat.Builder(getApplicationContext(), "$channelid")
//        val ii = Intent(mContext.getApplicationContext(), RootActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(mContext, 0, ii, 0)

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(body)
        bigText.setBigContentTitle(title)
        bigText.setSummaryText("")


        if (pendingIntent != null) {
            mBuilder.setContentIntent(pendingIntent)
        }
        mBuilder.setAutoCancel(autocancel)


        mBuilder.setOngoing(ongoing)
        mBuilder.setVibrate(vibratePattern)
        mBuilder.setLights(Color.parseColor(lightColor), lightOn, lightOff)
        mBuilder.setSmallIcon(smallIconId)
        mBuilder.setContentTitle(title)
        mBuilder.setContentText(body)
        mBuilder.priority = Notification.PRIORITY_MAX
        mBuilder.setStyle(bigText)
        mBuilder.setTicker(ticker)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel("${channelid}",
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH)
            channel.vibrationPattern = vibratePattern
            channel.enableLights(true)
            channel.lightColor = Color.parseColor(lightColor)
            mNotificationManager.createNotificationChannel(channel)
        }

        mNotificationManager.notify(0, mBuilder.build())

    }


    override fun onNotificationReady() {
        sendNotification(smallIconId, title, body, autocancel, lightColor, lightOn, lightOff,
                vibratePattern, pendingIntent, ongoing, ticker)
    }
}