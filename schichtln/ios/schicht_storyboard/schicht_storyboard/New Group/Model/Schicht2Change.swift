//
//  File.swift
//  schichtln
//
//  Created by Andreas Denk on 05.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation

 struct Schicht2Change:Codable{
    public var id:Int = 0
    public var day :Date = Date(timeIntervalSinceNow: 0)
    public var user : Int = -1
    public var position : Int = -1
    public var type :Int = -1
    public var state:State = .closed
    public var accept:Int = 2
    public var offered_to:Int?=nil
    public var note:String = ""
    public var trade_for_id:Int?=nil
    
    init(schicht:Schicht,
         id:Int?=nil,
         accept_id: Int?=nil,
         note: String?=nil,
         offered_to_id: Int?=nil,
         state: State?=nil,
         position_id: Int?=nil,
         type_id: Int?=nil,
         user_id: Int?=nil,
         day: Date?=nil,
         trade_for_id_id:Int?=nil) {
        
       
        self.id = id ?? schicht.id
        self.day = day ?? schicht.day
        self.user = user_id ?? schicht.user.id
        self.position = position_id ?? schicht.position.id
        self.type = type_id ?? schicht.type.id
        self.state = state ?? schicht.state
        self.accept = accept_id ?? schicht.accept.id
        self.offered_to = offered_to_id ?? schicht.offered_to?.id
        self.note = note ?? schicht.note
        self.trade_for_id = trade_for_id_id ?? schicht.trade_for_id
    }
}
