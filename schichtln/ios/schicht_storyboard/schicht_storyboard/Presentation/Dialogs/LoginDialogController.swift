//
//  LoginDialogController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 16.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit

class LoginDialogController: UIViewController {
    @IBOutlet weak var txt_name: UITextField!
    @IBOutlet weak var txt_pass: UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
