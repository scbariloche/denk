//
//  Extensions.swift
//  schichtln
//
//  Created by Andreas Denk on 06.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation

extension Date{
    func format(pattern:String="dd.MM.yyyy") -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat=pattern
        return (dateFormatter.string(from: self))   }
    
}
extension Formatter {
    static let iso8601: DateFormatter = {
        let formatter = DateFormatter()
        formatter.calendar = Calendar(identifier: .iso8601)
        formatter.locale = Locale(identifier: "de_DE_POSIX")
        formatter.timeZone = TimeZone(secondsFromGMT: TimeZone.current.secondsFromGMT())
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter
    }()
}
