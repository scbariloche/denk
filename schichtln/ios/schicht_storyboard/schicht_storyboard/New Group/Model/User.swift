//
//  User.swift
//  schichtln
//
//  Created by Andreas Denk on 04.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation


public struct User:Codable {
    public var id:Int = 40
    public var username: String = ""
    public var email: String = ""
    public var password: String = ""
    public var is_staff: Bool = false
    public var is_superuser: Bool = false
    public var groups: [Group] = [Group()]

}
