package com.example.andenk.schichtln.pojos

import com.example.andenk.schichtln.now
import java.io.Serializable
import java.util.*


/**
 * Created by andenk on 04.12.2017.
 */


data class User(
        var id: Int = 0,
        var username: String = "",
        var email: String = "",
        var password: String = "",
        var is_staff: Boolean = false,
        var is_superuser: Boolean = false,
        var groups: MutableList<Group> = mutableListOf<Group>()
) : Serializable {

    override fun toString(): String = username

}


data class Type(
        var id: Int = 0,
        var description: String = "",
        var position: Group = Group()
) : Serializable {
    override fun toString(): String = description

}

data class Accept(
        var id: Int = 2,
        var description: String = "",
        var icon: String = "") : Serializable


data class Group(
        var id: Int = 0,
        var name: String = "") : Serializable {
    override fun toString(): String = name

}


data class Schicht(
        var id: Long = 0,
        var day: Date = Date(System.currentTimeMillis()),
        var user: User? = User(),
        var position: Group = Group(),
        var type: Type = Type(),
        var state: String = "closed",
        var accept: Accept = Accept(),
        var offered_to: User? = null,
        var note: String = "",
        var trade_for_id: Long? = null) : Serializable

data class Schicht2Change(
        var id: Long = 0,
        var accept: Int = 2,
        var note: String = "",
        var offered_to: Int?,
        var state: String = "closed",
        var position: Int = -1,
        var type: Int = -1,
        var user: Int = -1,
        var day: Date = Date(now()),
        var trade_for_id: Long?
) {
    constructor(schicht: Schicht,
                id: Long = schicht.id,
                accept_id: Int = schicht.accept.id,
                note: String = schicht.note,
                offered_to_id: Int? = schicht.offered_to?.id,
                state: String = schicht.state,
                position_id: Int = schicht.position.id,
                type_id: Int = schicht.type.id,
                user_id: Int = schicht.user!!.id,
                day: Date = schicht.day,
                trade_for_id_id:Long?=schicht.trade_for_id) : this(





            id = id,
            accept = accept_id,
            note = note,
            offered_to = offered_to_id,
            state = state,
            position = position_id,
            type = type_id,
            user = user_id,
            day = day,
            trade_for_id = trade_for_id_id
    )


}