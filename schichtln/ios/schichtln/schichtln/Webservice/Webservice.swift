//
//  Webservice.swift
//  schichtln
//
//  Created by Andreas Denk on 06.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation
import Alamofire

let ip = "dugon.de"
let url = "http://\(ip)/"



enum WebserviceError:Int{
    case notFound = 404
    case serverError = 500
}


func get_user_by_name(name: String, on_success: @escaping (User) -> Void, on_error: @escaping (WebserviceError) -> Void) {
    Alamofire.request("\(url)userbyname/\(name)").responseJSON { response in
        
        switch response.response?.statusCode {
        case    200:
            guard  let user = try? JSONDecoder().decode(User.self, from: response.data!) else{
                on_error(.serverError)
                return
            }
            on_success(user)
            
        case   404 : on_error(.notFound)
        default : on_error(.serverError)
        }
        
        
        
    }
}

func get_user_by_id(id: Int, on_success: @escaping(User?) -> Void, on_error: @escaping (WebserviceError) -> Void) {
    Alamofire.request("\(url)user/\(id)").responseJSON { response in
        
        switch response.response?.statusCode {
        case   200 :
            guard  let user = try? JSONDecoder().decode(User.self, from: response.data!) else{
                on_error(.serverError)
                return
            }
            on_success(user)
            
        case  404 : on_error(.notFound)
        default : on_error(.serverError)
        }
    }
}

private func get_schicht_by_url(url:String, on_success: @escaping([Schicht]) -> Void, on_error: @escaping (WebserviceError) -> Void){
    
    Alamofire.request(url).responseJSON { response in
        
        switch response.response?.statusCode{
        case 200:
            let decoder = JSONDecoder()
            decoder.dateDecodingStrategy = .formatted(Formatter.iso8601)
            let schichts = try! decoder.decode([Schicht].self, from: response.data!)
            on_success(schichts)
            
            
        case 404: on_error(.notFound)
        default: on_error(.serverError)
        }
    }
    
}


func get_all_schicht_by_day(date: Date, on_success: @escaping([Schicht]) -> Void, on_error: @escaping (WebserviceError) -> Void) {
    get_schicht_by_url(url: "\(url)schichtenbyday/\(date.format(pattern: "yyyy-MM-dd"))", on_success: on_success, on_error: on_error)
}


func get_all_schichten_by_user(user: User, on_success: @escaping([Schicht]) -> Void, on_error: @escaping (WebserviceError) -> Void) {
    get_schicht_by_url(url: "\(url)schichtenbyuser/\(user.id)", on_success: on_success, on_error: on_error)
    
    
}


func get_all_schichten_for_trade(user_from: User, user_for: User, on_success: @escaping([Schicht]) -> Void, on_error: @escaping (WebserviceError) -> Void) {
    get_schicht_by_url(url: "\(url)schichtenfortradebyuser/\(user_from.id)/\(user_for.id)", on_success: on_success, on_error: on_error)
}


func get_all_schicht_by_position(group: Group, on_success: @escaping([Schicht]) -> Void, on_error: @escaping (WebserviceError) -> Void) {
    get_schicht_by_url(url: "\(url)schichtenbyposition/\(group.id)", on_success: on_success, on_error: on_error)
}

func get_open_schicht_by_user(user: User, on_success: @escaping([Schicht]) -> Void, on_error: @escaping (WebserviceError) -> Void) {
    get_schicht_by_url(url: "\(url)openschichtenbyuser/\(user.id)", on_success: on_success, on_error: on_error)

}


func delete_schicht(schicht: Schicht, on_success: @escaping() -> Void) {

    Alamofire.request("\(url)schicht/\(schicht.id)",method:.delete).response { response in
        on_success()
    }
}

func get_schicht_by_id(id: Int, on_success: @escaping( Schicht) -> Void, on_error: @escaping (WebserviceError) -> Void) {
    Alamofire.request("\(url)schicht/\(id)").responseJSON { response in
        
        switch response.response?.statusCode{
        case 200:
            let decoder = JSONDecoder()
            decoder.dateDecodingStrategy = .formatted(Formatter.iso8601)
            let schichts = try! decoder.decode(Schicht.self, from: response.data!)
            on_success(schichts)
            
            
        case 404: on_error(.notFound)
        default: on_error(.serverError)
        }
    }
    
}

func update_schicht(oldschicht: Schicht, newschicht: Schicht2Change, on_success: @escaping() -> Void, on_error: @escaping (WebserviceError) -> Void) {

    get_schicht_by_id(id:newschicht.id, on_success:{ it in
        if (it.is_same_as(schicht:oldschicht)) {
            
            let jsonData = try! JSONEncoder().encode(newschicht)
            var request = URLRequest(url: URL(string:"\(url)changeschicht/\(newschicht.id)/")!)
            request.httpMethod = HTTPMethod.put.rawValue
            request.setValue("application/json; charset=UTF-8", forHTTPHeaderField: "Content-Type")
            request.httpBody = jsonData
            
            
            Alamofire.request(request).responseJSON{it in
                on_success()
            }
            
            
       
        } else {
            on_error(.notFound)
        }
        
        
    }, on_error: { it in
        
    })


}


//func create_schicht(newschicht: Schicht2Change, on_success: @escaping() -> Void, on_error: @escaping (WebserviceError) -> Void) {
//    val request = Alamofire.post("\(url)schicht/")
//    request.headers["Content-Type"] = "application/json"
//    request.body(gson.toJson(newschicht)).response { response in
//        if (result.component2() == null) {
//            on_success()
//        } else {
//            on_error(result.component2()!!)
//        }
//    }
//}
//
//func get_all_types_(on_success: @escaping(Json) -> Void, on_error: @escaping (WebserviceError) -> Void) {
//    Alamofire.request("\(url)types/").responseJSON { response in
//        if (result.component2() == null) {
//            on_success(result.component1() as Json)
//        } else {
//            on_error(result.component2()!!)
//        }
//    }
//}
//
//func get_positions_by_user(user: User, on_success: @escaping(MutableList<Group>) -> Void, on_error: @escaping (WebserviceError) -> Void) {
//    Alamofire.request("\(url)positionbyuser/\(user.id}").responseJSON { response in
//        if (result.component2() == null) {
//            val json_array = (result.component1() as Json).array()
//
//            val positions: MutableList<Group> = mutableListOf()
//            var i = 0
//            while (i < json_array.length()) {
//                positions.add(gson.fromJson(json_array.get(i).toString(), Group::class.java))
//                i++
//            }
//            on_success(positions)
//        } else {
//            on_error(result.component2()!!)
//        }
//    }
//
//}
//
//
//
//
//func get_user_by_position(context: Context, position: Group, foo: (JSONArray) -> Void) {
//    Alamofire.request("\(url)userbyposition/\(position.id}/").responseJSON { response in
//        if (result.component2() == null) {
//            foo(result.component1()!!.array())
//        } else {
//            context.longToast(result.component2()!!.message.toString() + " in line \(line_number()}")
//        }
//    }
//
//
//}

