package com.example.andenk.schichtln.webservice

import android.content.Context
import com.example.andenk.schichtln.format
import com.example.andenk.schichtln.helper.gson
import com.example.andenk.schichtln.is_same_as
import com.example.andenk.schichtln.line_number
import com.example.andenk.schichtln.pojos.Group
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.pojos.Schicht2Change
import com.example.andenk.schichtln.pojos.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelError
import org.jetbrains.anko.longToast
import org.json.JSONArray
import java.util.*

/**
 * Created by andreas on 18.12.2017.
 */


// emulator loopback to host
//val ip = "10.0.2.2:8000"


// PC @ other
//    val ip = "10.148.58.212:8080"

// raspi @ home over dynDNS
//    val ip = "dugon.de:8888"

//dynDNS dugon.de
val ip = "dugon.de"
var url = "http://$ip/"


fun get_user_by_name(name: String, on_success: (User?) -> Unit, on_error: (FuelError?) -> Unit) {
    Fuel.get("${url}userbyname/$name").responseJson { request, response, result ->

        when (response.statusCode) {
            200 -> {
                on_success(com.example.andenk.schichtln.helper.gson.fromJson(
                        (result.component1() as Json).obj().toString(),
                        User::class.java))
            }
            404 -> on_success(null)
            else -> on_error(result.component2())
        }


    }
}

fun get_user_by_id(id: Int, on_success: (User?) -> Unit, on_error: (FuelError?) -> Unit) {
    Fuel.get("${url}user/$id").responseJson { request, response, result ->

        when (response.statusCode) {
            200 -> {
                on_success(com.example.andenk.schichtln.helper.gson.fromJson(
                        (result.component1() as Json).obj().toString(),
                        User::class.java))
            }
            404 -> on_success(null)
            else -> on_error(result.component2())
        }
    }
}


fun get_all_schicht_by_day(date: Date, on_success: (MutableList<Any>) -> Unit, on_error: (FuelError) -> Unit) {

    Fuel.get("http://dugon.de/schichtenbyday/${date.format("yyyy-MM-dd")}").responseJson { request, response, result ->
        if (result.component2() == null) {
            val json_array = (result.component1() as Json).array()

            val schichten_ret: MutableList<Any> = mutableListOf()
            var i = 0
            while (i < json_array.length()) {
                schichten_ret.add(gson.fromJson(json_array.get(i).toString(), Schicht::class.java))
                i++
            }
            on_success(schichten_ret)
        } else {
            on_error(result.component2()!!)
        }

    }


}


fun get_all_schichten_by_user(user: User, on_success: (MutableList<Any>) -> Unit, on_error: (FuelError) -> Unit) {

    Fuel.get("${url}schichtenbyuser/${user.id}").responseJson { request, response, result ->

        if (result.component2() == null) {
            val json_array = (result.component1() as Json).array()

            val schichten_ret: MutableList<Any> = mutableListOf()
            var i = 0
            while (i < json_array.length()) {
                schichten_ret.add(gson.fromJson(json_array.get(i).toString(), Schicht::class.java))
                i++
            }
            on_success(schichten_ret)
        } else {
            on_error(result.component2()!!)
        }


    }
}

fun get_all_schichten_for_trade(user_from: User, user_for: User, on_success: (MutableList<Any>) -> Unit, on_error: (FuelError) -> Unit) {
//TODO:

    Fuel.get("${url}schichtenfortradebyuser/${user_from.id}/${user_for.id}").responseJson { request, response, result ->

        if (result.component2() == null) {
            val json_array = (result.component1() as Json).array()

            var schichten_ret_original: MutableList<Schicht> = mutableListOf()
            var i = 0
            while (i < json_array.length()) {
                schichten_ret_original.add(gson.fromJson(json_array.get(i).toString(), Schicht::class.java))
                i++
            }
            get_open_schicht_by_user(user_from, on_success = {
                for (open_s in it) {
                    for (to_offer in schichten_ret_original) {
                        if ((open_s as Schicht).trade_for_id == (to_offer).id) {
                            schichten_ret_original.remove(to_offer)
                        }
                    }
                }
                on_success(schichten_ret_original as MutableList<Any>)
            }, on_error = {
                on_error(result.component2()!!)
            })

        } else {
            on_error(result.component2()!!)
        }


    }


}

fun get_all_schicht_by_position(group: Group, on_success: (MutableList<Any>) -> Unit, on_error: (FuelError) -> Unit) {

    Fuel.get("${url}schichtenbyposition/${group.id}").responseJson { request, response, result ->

        if (result.component2() == null) {
            val json_array = (result.component1() as Json).array()

            val schichten_ret: MutableList<Any> = mutableListOf()
            var i = 0
            while (i < json_array.length()) {
                schichten_ret.add(gson.fromJson(json_array.get(i).toString(), Schicht::class.java))
                i++
            }
            on_success(schichten_ret)
        } else {
            on_error(result.component2()!!)
        }


    }


}

fun get_open_schicht_by_user(user: User, on_success: (MutableList<Any>) -> Unit, on_error: (FuelError) -> Unit) {

    Fuel.get("${url}openschichtenbyuser/${user.id}").responseJson { request, response, result ->
        if (result.component2() == null) {
            val json_array = (result.component1() as Json).array()

            val schichten_ret: MutableList<Any> = mutableListOf()
            var i = 0
            while (i < json_array.length()) {
                schichten_ret.add(gson.fromJson(json_array.get(i).toString(), Schicht::class.java))
                i++
            }
            on_success(schichten_ret)
        } else {
            on_error(result.component2()!!)
        }
    }

}


fun delete_schicht(schicht: Schicht, on_success: () -> Unit, on_error: (errorString: String) -> Unit) {
    get_schicht_by_id(schicht.id,
            on_success = {
                if (it.is_same_as(schicht)) {
                    Fuel.delete("${url}schicht/${schicht.id}").response { request, response, result ->
                        on_success()
                    }
                } else {
                    on_error("schicht wurde schon geändert")
                }
            }, on_error = {
        on_error(it.localizedMessage)

    })
}

fun get_schicht_by_id(id: Long, on_success: (schicht: Schicht) -> Unit, on_error: (FuelError) -> Unit) {
    Fuel.get("${url}schicht/$id").responseJson { request, response, result ->
        if (result.component2() == null) {
            val json = (result.component1() as Json).obj()
            on_success(gson.fromJson(json.toString(), Schicht::class.java))
        } else {
            on_error(result.component2()!!)
        }
    }
}

fun update_schicht(oldschicht: Schicht, newschicht: Schicht2Change, on_success: () -> Unit, on_error: (errorString: String) -> Unit, check_if_changed: Boolean = true) {

    get_schicht_by_id(newschicht.id,
            on_success = {
                if (it.is_same_as(oldschicht) || !check_if_changed) {
                    val request = Fuel.post("${url}changeschicht/${newschicht.id}/")
                    request.headers["Content-Type"] = "application/json"
                    request.body(gson.toJson(newschicht)).response { request, response, result ->
                        if (result.component2() == null) {
                            on_success()
                        } else {
                            on_error(result.component2()!!.localizedMessage)
                        }
                    }
                } else {
                    on_error("schicht wurde schon geändert")
                }
            },
            on_error = { on_error("schicht wurde schon geändert") })


}


fun create_schicht(newschicht: Schicht2Change, on_success: () -> Unit, on_error: (FuelError) -> Unit) {
    val request = Fuel.post("${url}schicht/")
    request.headers["Content-Type"] = "application/json"
    request.body(gson.toJson(newschicht)).response { request, response, result ->
        if (result.component2() == null) {
            on_success()
        } else {
            on_error(result.component2()!!)
        }
    }
}

fun get_all_types_(on_success: (Json) -> Unit, on_error: (FuelError) -> Unit) {
    Fuel.get("${url}types/").responseJson { request, response, result ->
        if (result.component2() == null) {
            on_success(result.component1() as Json)
        } else {
            on_error(result.component2()!!)
        }
    }
}

fun get_positions_by_user(user: User, on_success: (MutableList<Group>) -> Unit, on_error: (FuelError) -> Unit) {
    Fuel.get("${url}positionbyuser/${user.id}").responseJson { request, response, result ->
        if (result.component2() == null) {
            val json_array = (result.component1() as Json).array()

            val positions: MutableList<Group> = mutableListOf()
            var i = 0
            while (i < json_array.length()) {
                positions.add(gson.fromJson(json_array.get(i).toString(), Group::class.java))
                i++
            }
            on_success(positions)
        } else {
            on_error(result.component2()!!)
        }
    }

}


fun get_user_by_position(context: Context, position: Group, foo: (JSONArray) -> Unit) {
    Fuel.get("${url}userbyposition/${position.id}/").responseJson { request, response, result ->
        if (result.component2() == null) {
            foo(result.component1()!!.array())
        } else {
            context.longToast(result.component2()!!.message.toString() + " in line ${line_number()}")
        }
    }


}




