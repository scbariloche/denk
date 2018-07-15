//
//  PlanDetailViewControllerTableViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 12.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit


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
    var user = User()
    var schichts: [Schicht] = []

    override func viewDidLoad() {
        super.viewDidLoad()
//        user = StoredValues.user
        personalTableView.tableFooterView = UIView()
        get_all_schichten_by_user(
            user: user,on_success: {
                schichts in self.schichts = schichts
                self.personalTableView.reloadData()
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
        let schicht = schichts[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "personalViewCell", for: indexPath) as! PersonalViewCell
        cell.lblType.text=schicht.type.description
        cell.lblDate.text = self.dateFormatter.string(from: schicht.day)

        cell.img_acc.image = UIImage(named: schicht.accept.icon)
        cell.img_state.image = UIImage(named: schicht.state.rawValue)
        return cell
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
