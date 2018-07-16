//
//  PlanDetailViewControllerTableViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 12.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit
import SelectionDialog
import PopupDialog



class PersonalViewCell:UITableViewCell{
    @IBOutlet weak var img_acc: UIImageView!
    @IBOutlet weak var img_state: UIImageView!

    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblType: UILabel!
}
class PersonalViewController: UITableViewController {
    fileprivate lazy var dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "EE. dd.MM.yy"
        return formatter
    }()

    @IBOutlet var personalTableView: UITableView!

    var schichts: [Schicht] = []

    fileprivate func init_view(for user: User) {
        personalTableView.tableFooterView = UIView()
        get_all_schichten_by_user(
            user: user,on_success: {
                schichts in self.schichts = schichts
                self.personalTableView.reloadData()
        },on_error: {
            error in

        } )
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        if let   user = StoredValues.user{
            init_view(for: user)
            
        } else {
            LoginService.callLoginDialog(from: self, completion: {user in self.init_view(for: user)})
        }


        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        //         self.navigationItem.rightBarButtonItem = self.editButtonItem
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source



    override  func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let selectedSchicht = schichts[indexPath.row]

        switch selectedSchicht.accept.description {
        case .accepted:
            let popup = SelectionDialog(title: "was willst du tun?", closeButtonTitle: "close")
            //            "offen anbieten", "jemanden anbieten", "zum Tausch anbieten"
            popup.addItem(item: "offen anbieten", didTapHandler: {
                update_schicht(oldschicht: selectedSchicht,
                               newschicht: Schicht2Change(schicht: selectedSchicht, state: .open),
                               on_success: {
                                self.init_view(for: StoredValues.user!)
                                sendMessage(to: "position\(selectedSchicht.type.id)", body: "nimm!")
                                popup.close()
                },
                               on_error: {
                                error in print(error)}
                )
            }
            )
            popup.show()
            print("ok")
        case .pending:
            handlePending(selectedSchicht)
        case .deleted:
           handleDeleted(selectedSchicht)
        }

    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return schichts.count
    }


    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let schicht = schichts[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "personalViewCell", for: indexPath) as! PersonalViewCell
        cell.lblType.text=schicht.type.description
        cell.lblDate.text = self.dateFormatter.string(from: schicht.day)

        cell.img_acc.image = UIImage(named: schicht.accept.icon)
        cell.img_state.image = UIImage(named: schicht.state.rawValue)
        return cell
    }

    fileprivate func handlePending(_ selectedSchicht: Schicht) {
        let popup = PopupDialog(title: "diese Schicht wurde geändert", message: "", image: nil, buttonAlignment: .horizontal, transitionStyle: .zoomIn , preferredWidth: CGFloat(80), gestureDismissal: true, hideStatusBar: true, completion:  nil)
        let okBtn = DefaultButton(title: "OK", dismissOnTap:true){
            update_schicht(oldschicht: selectedSchicht, newschicht: Schicht2Change(schicht:selectedSchicht,accept_id:1), on_success: {
                self.init_view(for: StoredValues.user!)

            }, on_error: {error in print(error)})

        }
        popup.addButton(okBtn)
        self.present(popup, animated: true, completion: nil)
    }
    fileprivate func handleDeleted(_ selectedSchicht: Schicht) {
        let popup = PopupDialog(title: "diese Schicht wurde gestrichen", message: "", image: nil, buttonAlignment: .horizontal, transitionStyle: .zoomIn , preferredWidth: CGFloat(80), gestureDismissal: true, hideStatusBar: true, completion:  nil)
        let okBtn = DefaultButton(title: "OK", dismissOnTap:true){
            delete_schicht(schicht: selectedSchicht, on_success: {
                self.init_view(for: StoredValues.user!)
            })

        }
        popup.addButton(okBtn)
        self.present(popup, animated: true, completion: nil)
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
