//
//  Message.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 16.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation
import Alamofire
public struct Message: Codable {
    var data: MessageData
    var to: String
}

public struct MessageData:Codable{
    var title: String
    var body:String
}

public func sendMessage(to: String, title: String="CINEMA", body: String){
    var request = URLRequest(url: URL(string: "https://fcm.googleapis.com/fcm/send")!)

    request.httpMethod = HTTPMethod.post.rawValue
    request.setValue("application/json; charset=UTF-8", forHTTPHeaderField: "Content-Type")
    request.setValue("key=AAAAeaDL9ss:APA91bGJJwBuwpBul2mr5zeNBpKOMoJSvFjwV1EMhtFhXtFCoHTjdFr_Eg_MluQ5epIAFDwEY1Ay1i-zpkmiNfXOUDVlYFk_6rMCR1nBQwIwZ4tXoJQRvbV0za4WdgdtKRVIqQC1UMWp", forHTTPHeaderField: "Authorization")

    let headers: HTTPHeaders = [
        "Authorization": "key=AAAAeaDL9ss:APA91bGJJwBuwpBul2mr5zeNBpKOMoJSvFjwV1EMhtFhXtFCoHTjdFr_Eg_MluQ5epIAFDwEY1Ay1i-zpkmiNfXOUDVlYFk_6rMCR1nBQwIwZ4tXoJQRvbV0za4WdgdtKRVIqQC1UMWp",
        "Content-Type": "application/json"
    ]
    
    let parameters: Parameters = [
        "data": [
            "title": title,
            "body": body
        ],
        "to": "/topics/\(to)"
    ]
    Alamofire.request( "https://fcm.googleapis.com/fcm/send", method: HTTPMethod.post, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseString{it in
        print(it)
    }
}
