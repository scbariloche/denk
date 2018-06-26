package com.example.andenk.schichtln.notification

import com.example.andenk.schichtln.helper.gson
import com.github.kittinunf.fuel.Fuel
import java.io.Serializable

/**
 * Created by andenk on 29.05.2018.
 */

val SERVERKEY = "AAAAeaDL9ss:APA91bGJJwBuwpBul2mr5zeNBpKOMoJSvFjwV1EMhtFhXtFCoHTjdFr_Eg_MluQ5epIAFDwEY1Ay1i-zpkmiNfXOUDVlYFk_6rMCR1nBQwIwZ4tXoJQRvbV0za4WdgdtKRVIqQC1UMWp"

data class Message(
        var data: MessageData,
        var to: String
) : Serializable

data class MessageData(
        var body: String,
        var title: String
) : Serializable

fun sendMessage(to: String, title: String="CINEMA", body: String) {
    val request = Fuel.post("https://fcm.googleapis.com/fcm/send")
    request.headers["Content-Type"] = "application/json"
    request.headers["Authorization"] = "key=$SERVERKEY"
    val body = gson.toJson(Message(to = "/topics/$to", data = MessageData(body = body, title = title)))
    request.body(body)
            .response { request, response, result ->
                print("awdsfaewf")
            }
}