//
//  LoginService.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 15.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import Foundation
import UIKit
import PopupDialog
import IDZSwiftCommonCrypto
//import Security
//import CommonCrypto


class LoginService {



    public static func callLoginDialog( from view: UIViewController?, completion: @escaping(User) -> Void){
        // Create the dialog

        let loginView = LoginDialogController()



        let popup = PopupDialog(viewController: loginView, gestureDismissal: false, completion: {})
        let btnLogin = DefaultButton(title: "login", dismissOnTap: false) {
            guard let name = loginView.txt_name.text,
            let pass = loginView.txt_pass.text else {
                popup.shake()
                return
            }
            get_user_by_name(name: name, on_success: { user in

                if  user.isPassOK(input: pass) {
                    StoredValues.user = user
                    popup.dismiss()
                    completion(user)
                } else {
                    popup.shake()
                }

            }
                , on_error: {
                    error in popup.shake()})

        }

        popup.addButtons([btnLogin])



        // Present dialog
        view?.present(popup, animated: true, completion: nil)

    }

}
private extension User{
    func isPassOK(input:String) -> Bool{
        return self.comparePass(pass: input, hashedPass: self.password)
    }
    private func comparePass(pass: String, hashedPass: String)->Bool{
        let parts = hashedPass.split(separator: "$")

        let salt = String(parts[2])
        let iterations = Int(parts[1]) as! Int
        let algorithm = String(parts[0])

        let hashedInput:String = hash(pass: pass, salt: salt, iterations: iterations,algorithm: algorithm)
        return hashedPass.prefix(50) == hashedInput.prefix(50)
    }
    private func hash(pass: String, salt: String, iterations: Int, algorithm: String) -> String {


        let hashedInput = pbkdf2SHA1(password: pass, salt: salt.data(using: .utf8)!, keyByteCount: 256, rounds: iterations)?.base64EncodedData()
        return ([algorithm,String(iterations),salt, (String(data:hashedInput!, encoding: .utf8)!)]).joined(separator:"$")
    }

    func pbkdf2SHA1(password: String, salt: Data, keyByteCount: Int, rounds: Int) -> Data? {
        return pbkdf2(hash:CCPBKDFAlgorithm(kCCPRFHmacAlgSHA1), password:password, salt:salt, keyByteCount:keyByteCount, rounds:rounds)
    }

    func pbkdf2(hash :CCPBKDFAlgorithm, password: String, salt: Data, keyByteCount: Int, rounds: Int) -> Data? {
        let passwordData = password.data(using:String.Encoding.utf8)!
        var derivedKeyData = Data(repeating:0, count:keyByteCount)

        let derivationStatus = derivedKeyData.withUnsafeMutableBytes {derivedKeyBytes in
            salt.withUnsafeBytes { saltBytes in

                CCKeyDerivationPBKDF(
                    CCPBKDFAlgorithm(kCCPBKDF2),
                    password, passwordData.count,
                    saltBytes, salt.count,
                    hash,
                    UInt32(rounds),
                    derivedKeyBytes, derivedKeyData.count)
            }
        }
        if (derivationStatus != 0) {
            print("Error: \(derivationStatus)")
            return nil;
        }

        return derivedKeyData
    }
}
