//
//  UserDefaults.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 15.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation
import UIKit

class StoredValues{

    static var user:User?{
        set {

            if newValue == nil{
                UserDefaults.standard.removeObject(forKey: "user")
            }else{

                do{
                    let valueData = try PropertyListEncoder().encode(newValue)
                    UserDefaults.standard.set(valueData, forKey: "user")
                }catch{
                    print("qf")
                }
            }
        }
        get {
            do {
                guard let restoredValueData = UserDefaults.standard.data(forKey: "user") else {
           
                    return nil
                }

                let user = try PropertyListDecoder().decode(User.self, from: restoredValueData)
                return user
            } catch {
                return nil
            }
        }
    }

}
