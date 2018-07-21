//
//  PlanDetailViewControllerTableViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 12.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit
import PopupDialog
import SelectionDialog

class TradeViewCell:UITableViewCell{
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblType: UILabel!
    @IBOutlet weak var lblName: UILabel!

    @IBOutlet weak var img_acc: UIImageView!
    @IBOutlet weak var img_state: UIImageView!
}
class TradeViewController: UITableViewController {
    @IBOutlet var tradeTableView: UITableView!


    var schichts: [Schicht] = []



    override func viewDidLoad() {
        super.viewDidLoad()
        if let user = StoredValues.user{
            init_view(for: user)
        }else{
            LoginService.callLoginDialog(from: self, completion: {user in self.init_view(for: user)}
            )
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

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let selected_schicht = schichts[indexPath.row]
         if (selected_schicht.user.id != StoredValues.user?.id) {
            switch selected_schicht.state{
            case .closed :
                fallthrough
            case .for_trade:
                if (selected_schicht.trade_for_id == nil) {
                    get_all_schichten_for_trade(user_from: StoredValues.user!, user_for: selected_schicht.user, on_success: {
                        (schichts) in
                        let selection_popup = SelectionDialog(title: "für", closeButtonTitle: "close")
                        for schicht_for_trade in schichts {
                            selection_popup.addItem(item: "\(self.dateFormatter.string(from: schicht_for_trade.day)) \(schicht_for_trade.type.description)", didTapHandler: {
                                update_schicht(oldschicht: selected_schicht, newschicht: Schicht2Change(schicht:selected_schicht,trade_for_id_id: schicht_for_trade.id),
                                               on_success: {
                                                selection_popup.close()
                                                self.init_view(for: StoredValues.user!)
//                                                tableView.reloadData()
                                }, on_error: { (error) in
                                    selection_popup.close()
                                    print(error)
                                    self.init_view(for: StoredValues.user!)
                                    tableView.reloadData()
                                })
                            })

                        }
                        selection_popup.show()
                    }) { (error) in
                        print(error)
                        tableView.reloadData()
                    }
                } else {
                    let popup : PopupDialog = PopupDialog(title: "für diese Schicht wurde bereits ein Angebot abgegeben", message: "")
                    popup.addButton(PopupDialogButton(title: "OK", action: {popup.dismiss()}))
                    self.present(popup,animated: true)
                     }
            case .offered :
                let popup : PopupDialog = PopupDialog(title: "möchtest du diese Schicht annehmen?", message: "")
                popup.addButton(PopupDialogButton(title: "Ja", action: {update_schicht(oldschicht: selected_schicht, newschicht: Schicht2Change(schicht: selected_schicht, offered_to_id : nil, state: .closed, user_id: StoredValues.user?.id, trade_for_id_id:nil), on_success: {
                    popup.dismiss()
                    tableView.reloadData()
                }, on_error: { (error) in
                    print(error)
                    tableView.reloadData()
                })}))
                self.present(popup,animated: true)
            case .open:
                let popup : PopupDialog = PopupDialog(title: "möchtest du diese Schicht annehmen?", message: "")
                popup.addButton(PopupDialogButton(title: "Ja", action: {update_schicht(oldschicht: selected_schicht, newschicht: Schicht2Change(schicht: selected_schicht), on_success: {
                    popup.dismiss()
                    tableView.reloadData()
                }, on_error: { (error) in
                    print(error)
                    tableView.reloadData()
                })}))
                self.present(popup,animated: true)

            }
        }







    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return schichts.count
    }
    fileprivate lazy var dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "EE dd.MM.yy"
        formatter.locale = Locale(identifier: "de_DE")
        return formatter
    }()

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let schicht = schichts[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "tradeViewCell", for: indexPath) as! TradeViewCell
        cell.lblDate.text = dateFormatter.string(from: schicht.day)
        cell.lblType.text = schicht.type.description
        cell.lblName.text = schicht.user.username

        cell.img_acc.image = UIImage(named: schicht.accept.icon)
        cell.img_state.image = UIImage(named: schicht.state.rawValue)
        if (schicht.state == State.for_trade && schicht.trade_for_id != nil){
            cell.img_state.image = UIImage(named: "for_trade_with_offer")
        }


        return cell
    }
    fileprivate func init_view(for user: User) {
        tradeTableView.tableFooterView = UIView()
        get_open_schicht_by_user(
            user: user,on_success: {
                schichts in self.schichts = schichts
                self.tradeTableView.reloadData()
        },on_error: {
            error in

        })
    }


}
