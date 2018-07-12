//
//  PlanDetailViewControllerTableViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 12.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit


class TradeViewCell:UITableViewCell{

}
class TradeViewController: UITableViewController {


    var user = User()
    var schichts: [Schicht] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        get_open_schicht_by_user(
            user: user,on_success: {
                schichts in self.schichts = schichts
//                self.planTableView.reloadData()
        },on_error: {
            error in

        })


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


    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "tradeViewCell", for: indexPath) as! TradeViewCell
       

        return cell
    }



}
