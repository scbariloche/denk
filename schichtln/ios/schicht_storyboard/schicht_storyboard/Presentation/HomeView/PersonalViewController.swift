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
        formatter.dateFormat = "EE dd.MM.yy"
        formatter.locale = Locale(identifier: "de_DE")
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
            popup.addItem(getOpenOptionItem(selectedSchicht, popup, tableView))
            popup.addItem(getOfferOptionItem(selectedSchicht, popup, tableView))
            popup.addItem(getTradeOptionItem(selectedSchicht, popup, tableView))
            popup.show()
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

    // MARK: SelectionOptions

    fileprivate func getOpenOptionItem(_ selectedSchicht: Schicht, _ popup: SelectionDialog, _ tableView: UITableView) -> SelectionDialogItem {
        return SelectionDialogItem(item: "offen anbieten", didTapHandler: {
            update_schicht(oldschicht: selectedSchicht,
                           newschicht: Schicht2Change(schicht: selectedSchicht, state: .open),
                           on_success: {
                            self.init_view(for: StoredValues.user!)
                            sendMessage(to: "position\(selectedSchicht.position.id)",title: "\(selectedSchicht.type.description) zu vergeben", body: "\(selectedSchicht.type.description) am \(self.dateFormatter.string(from: selectedSchicht.day))")
                            popup.close()
                            tableView.reloadData()
            },
                           on_error: {
                            error in print(error)
                            popup.close()

                            tableView.reloadData()
            }
            )
        }
        )
    }
    fileprivate func getOfferOptionItem(_ selectedSchicht: Schicht, _ popup: SelectionDialog, _ tableView: UITableView) -> SelectionDialogItem {
        return SelectionDialogItem(item:"jemandem anbieten", didTapHandler:{
            get_user_by_position(position: selectedSchicht.position, on_success: { (user) in
                let user_selection_popup = SelectionDialog(title: "an wen?", closeButtonTitle: "close")
                for new_user in user{
                    user_selection_popup.addItem(item: new_user.username, didTapHandler: {
                        update_schicht(oldschicht: selectedSchicht,
                                       newschicht: Schicht2Change(schicht: selectedSchicht, offered_to_id:new_user.id, state: .offered),
                                       on_success: {
                                        self.init_view(for: StoredValues.user!)
                                        sendMessage(to: "\(new_user.id)", body: "willst du \(selectedSchicht.type.description) am \(self.dateFormatter.string(from: selectedSchicht.day))?")
                                        user_selection_popup.close()
                                        popup.close()
                                        tableView.reloadData()
                        },
                                       on_error: {
                                        error in print(error)
                                        tableView.reloadData()
                                        popup.close()
                                        user_selection_popup.close()
                        }
                        )

                    })
                }
                user_selection_popup.show()
            }, on_error: {
                error in print(error)
                popup.close()
})
        })
    }
    fileprivate func getTradeOptionItem(_ selectedSchicht: Schicht, _ popup: SelectionDialog, _ tableView: UITableView) -> SelectionDialogItem {
        return SelectionDialogItem(item: "zum Tausch anbieten", didTapHandler: {
            update_schicht(oldschicht: selectedSchicht,
                           newschicht: Schicht2Change(schicht: selectedSchicht, state: .for_trade),
                           on_success: {
                            self.init_view(for: StoredValues.user!)
                            sendMessage(to: "position\(selectedSchicht.position.id)", title: "\(selectedSchicht.type.description) zum tauschen", body: "\(selectedSchicht.type.description) am \(self.dateFormatter.string(from: selectedSchicht.day))")
                            popup.close()
                            tableView.reloadData()
            },
                           on_error: {
                            error in print(error)
                            popup.close()

                            tableView.reloadData()
            }
            )
        }
        )
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
