//
//  PlanDetailViewControllerTableViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 09.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit


class PlanDetailCell:UITableViewCell{
    
    @IBOutlet weak var lbl_date: UILabel!
    @IBOutlet weak var lbl_type: UILabel!
    
    @IBOutlet weak var img_acc: UIImageView!
    @IBOutlet weak var img_state: UIImageView!

   }
class PlanDetailViewController: UITableViewController {
 
    
    @IBOutlet var planTableView: UITableView!
    var position = Group()
    var schichts: [Schicht] = []
   
    override func viewDidLoad() {
        super.viewDidLoad()
        planTableView.tableFooterView = UIView()
        get_all_schicht_by_position(
            group: position,on_success: {
                schichts in self.schichts = schichts
                self.planTableView.reloadData()
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

 
    fileprivate lazy var dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "EE. dd.MM.yy"
        return formatter
    }()
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "planDetailCell", for: indexPath) as! PlanDetailCell
        let schicht = schichts[indexPath.row]
        cell.lbl_date.text = dateFormatter.string(from: schicht.day)
        cell.lbl_type.text = schicht.type.description

        cell.img_acc.image = UIImage(named: schicht.accept.icon)
        cell.img_state.image = UIImage(named: schicht.state.rawValue)


        return cell
    }
 

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
