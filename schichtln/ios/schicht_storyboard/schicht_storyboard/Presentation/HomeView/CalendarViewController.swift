//
//  CalendarViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 12.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit
import FSCalendar




class CalendarViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, FSCalendarDataSource, FSCalendarDelegate, UIGestureRecognizerDelegate  {
    var daily_schichts:[Schicht] = []
    var user_schichts:[Date] = []
    @IBOutlet weak var calendarView: FSCalendar!

    @IBOutlet weak var calendarTableView: UITableView!
    @IBOutlet weak var calendarHeightConstraint: NSLayoutConstraint!


    override func viewDidLoad() {
        super.viewDidLoad()

        if let user = StoredValues.user {
            init_view(for: user)
        }else{
            LoginService.callLoginDialog(from: self, completion:{user in self.init_view(for: user)} )
        }


    }

    fileprivate func init_view(for user: User){
        calendarTableView.tableFooterView = UIView()
        self.calendarView.addGestureRecognizer(self.scopeGesture)
        calendarView.scope = .month

        get_all_schichten_by_user(user: user,
                                  on_success: {
                                    schichts in
                                    self.user_schichts.removeAll()
                                    for schicht in schichts{
                                        self.user_schichts.append(schicht.day)
                                    }
                                    self.calendarView.reloadData()
        }) {
            (error) in
            print(error)
        }
        get_all_schicht_by_day(date: Date(timeIntervalSinceNow: 0), on_success: {
            schichts in self.daily_schichts = schichts
            self.calendarTableView.reloadData()
        }, on_error: {error in print(error)})
    }

    fileprivate var dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy/MM/dd"
        return formatter
    }()

    fileprivate lazy var scopeGesture: UIPanGestureRecognizer = {
        [unowned self] in
        let panGesture = UIPanGestureRecognizer(target: self.calendarView, action: #selector(self.calendarView.handleScopeGesture(_:)))
        panGesture.delegate = self
        panGesture.minimumNumberOfTouches = 1
        panGesture.maximumNumberOfTouches = 2
        return panGesture
        }()

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()

        // Dispose of any resources that can be recreated.
    }

    func calendar(_ calendar: FSCalendar, boundingRectWillChange bounds: CGRect, animated: Bool) {
        self.calendarHeightConstraint.constant = bounds.height
        self.view.layoutIfNeeded()
    }
    func calendar(_ calendar: FSCalendar, hasEventFor date: Date) -> Bool {
        return user_schichts.contains(date)
    }

    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        get_all_schicht_by_day(date: date, on_success: {
            schichts in
            self.daily_schichts = schichts
            self.calendarTableView.reloadData()}, on_error: {it in print(it)})

        print("did select date \(self.dateFormatter.string(from: date))")
        let selectedDates = calendar.selectedDates.map({self.dateFormatter.string(from: $0)})
        print("selected dates is \(selectedDates)")
        if monthPosition == .next || monthPosition == .previous {
            calendar.setCurrentPage(date, animated: true)
        }
    }

    func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
        print("\(self.dateFormatter.string(from: calendar.currentPage))")
    }


    //     MARK: - TableView
    func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return daily_schichts.count
    }


    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
         let schicht = daily_schichts[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "calendarViewCell", for: indexPath) as! CalendarViewCell
        cell.lbl_name.text = schicht.user.username
        cell.lbl_type.text = schicht.type.description

        cell.img_acc.image = UIImage(named: schicht.accept.icon)
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

class CalendarViewCell:UITableViewCell{
    @IBOutlet weak var lbl_name: UILabel!
    @IBOutlet weak var lbl_type: UILabel!

    @IBOutlet weak var img_acc: UIImageView!
    
}
