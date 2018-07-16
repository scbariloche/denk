//
//  PlanDetailViewControllerTableViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 12.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit


class TradeViewCell:UITableViewCell{
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblType: UILabel!
    @IBOutlet weak var lblName: UILabel!

    @IBOutlet weak var img_state: UIImageView!
    @IBOutlet weak var img_acc: UIImageView!
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

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return schichts.count
    }
    fileprivate lazy var dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "EE. dd.MM.yy"
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
