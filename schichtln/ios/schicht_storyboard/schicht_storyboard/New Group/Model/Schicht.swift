//
//  Schicht.swift
//  schichtln
//
//  Created by Andreas Denk on 05.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation

public struct Schicht:Codable{
    public var id:Int = 0
    public var day :Date = Date(timeIntervalSinceNow: 0)
    public var user : User = User()
    public var position : Group = Group()
    public var type :Type = Type()
    public var state:State = .closed
    public var accept:Accept = Accept()
    public var offered_to:User? = User()
    public var note:String = ""
    public var trade_for_id:Int? = 0
    
}

public enum State: String , Codable{
    case closed = "closed"
    case open = "open"
    case offered = "offered"
    case for_trade = "for_trade"
}

extension Schicht{
    func is_same_as(schicht:Schicht) -> Bool{
        return (self.id == schicht.id
        && self.accept.id == schicht.accept.id
        && self.day == schicht.day
        && self.offered_to?.id == schicht.offered_to?.id
        && self.position.id == schicht.position.id
        && self.state == schicht.state
        && self.trade_for_id == schicht.trade_for_id
        && self.type.id == self.type.id
        && self.user.id == self.user.id)
    }
}
