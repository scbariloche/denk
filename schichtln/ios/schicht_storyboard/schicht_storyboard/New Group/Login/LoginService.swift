//
//  LoginService.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 15.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation
import UIKit
import  PopupDialog

class LoginService {
    public static func callLoginDialog( from view: UIViewController?){
        // Create the dialog
        let popup = PopupDialog(title: "anmelden?", message: "wirklich", image: nil, gestureDismissal: false, completion: {})
        popup.addButton(PopupDialogButton(title: "y", height: 20, dismissOnTap: false, action: {}))
        let buttonTwo = DefaultButton(title: "ADMIRE CAR", dismissOnTap: false) {
            popup.dismiss()
            print("What a beauty!")
        }

        let buttonThree = DefaultButton(title: "BUY CAR", height: 60) {
            print("Ah, maybe next time :)")
        }
        popup.addButtons([buttonTwo, buttonThree])



        // Present dialog
        view?.present(popup, animated: true, completion: nil)

        // Get the default view controller and cast it
        // Unfortunately, casting is necessary to support Objective-C


    }
}
