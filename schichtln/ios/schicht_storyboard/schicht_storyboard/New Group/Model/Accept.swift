//
//  Accept.swift
//  schichtln
//
//  Created by Andreas Denk on 05.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation

public enum acceptDescription: String, Codable{
    case accepted = "accepted"
    case deleted = "deleted"
    case pending = "pending"

}

public struct Accept:Codable{
    public var id:Int = 0
    public var description:acceptDescription = .accepted
    public var icon :String = ""
}
