//
//  Type.swift
//  schichtln
//
//  Created by Andreas Denk on 05.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation

public struct Type:Codable{
    public var id:Int = 0
    public var description:String=""
    public var position :Group=Group()
}
