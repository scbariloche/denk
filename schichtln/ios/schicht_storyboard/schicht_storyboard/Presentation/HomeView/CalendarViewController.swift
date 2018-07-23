//
//  CalendarViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 12.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit
import FSCalendar
import SelectionDialog
import PopupDialog




class CalendarViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, FSCalendarDataSource, FSCalendarDelegate, UIGestureRecognizerDelegate  {
    var daily_schichts:[Schicht] = []
    var user_schichts:[Date] = []
    @IBOutlet weak var calendarView: FSCalendar!

    @IBOutlet weak var calendarTableView: UITableView!
    @IBOutlet weak var calendarHeightConstraint: NSLayoutConstraint!
    @IBAction func action_plus(_ sender: Any) {
        print("plus!")
        let date = calendarView.selectedDate ?? Date(timeIntervalSinceNow: 0)
        let popup = SelectionDialog(title: "\(self.dateFormatter().string(from: date))", closeButtonTitle: "")
        get_all_types_(on_success: { (types) in
            for type in types{
                popup.addItem(item: type.description, didTapHandler: {
                    let popup_names = SelectionDialog(title: "an wen?", closeButtonTitle: "")
                    get_user_by_position(position: type.position, on_success: { (users) in
                        for user in users{
                            popup_names.addItem(item: user.username, didTapHandler: {
                                let schicht = Schicht()
                                create_schicht(newschicht: Schicht2Change(schicht: schicht,  accept_id: 2,  state: .closed, position_id: type.position.id, type_id: type.id, user_id: user.id, day: date), on_success: {
                                    popup_names.close()
                                    popup.close()
                                    sendMessage(to: "\(user.id)", title: "Du hast eine neue Schicht", body:"\(type.description) am \(self.dateFormatter().string(from: date))" )
                                    self.init_view(for: StoredValues.user!)
                                }, on_error: { (error) in
                                    print(error)
                                    popup_names.close()
                                    popup.close()
                                    self.init_view(for: StoredValues.user!)
                                })
                            })
                        }
                        popup_names.show()
                    }, on_error: { (error) in
                        print(error)
                        popup_names.close()
                        popup.close()
                        self.init_view(for: StoredValues.user!)
                    })
                })
            }
            popup.show()
        }, on_error: { (error) in
            popup.close()
            self.init_view(for: StoredValues.user!)
            print(error)
        })
    }
    @IBOutlet weak var btn_plus: UIButton!


    override func viewDidLoad() {
        super.viewDidLoad()

        if let user = StoredValues.user {
            init_view(for: user)
        }else{
            LoginService.callLoginDialog(from: self, completion:{user in self.init_view(for: user)} )
        }


    }

    fileprivate func init_view(for user: User){
        calendarView.bringSubview(toFront: btn_plus)
        btn_plus.isHidden = !user.is_superuser


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

    fileprivate func dateFormatter(_ format :String = "dd.MM.yyyy")-> DateFormatter  {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter
    }

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
        if monthPosition == .next || monthPosition == .previous {
            calendar.setCurrentPage(date, animated: true)
        }
    }

    func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
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
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let schicht = self.daily_schichts[indexPath.row]
        if let user = StoredValues.user{
            if user.is_superuser{
                var message = "Diese Schicht streichen?"
                var accept_id = 3
                var   title = "Du hast am \(self.dateFormatter().string(from: schicht.day)) frei"
                var  body = "Die Vertretung hat '\(schicht.type.description)' gestrichen. Bitte bestätige dies"
                
                
                if schicht.accept.description == acceptDescription.deleted {
                    message = "diese Schicht wieder aktivieren?"
                    accept_id = 2
                    title = "Du hast eine unbestätigte Änderung"
                    body = "Bitte bestätige, dass du am \(self.dateFormatter().string(from: schicht.day)) arbeiten kannst"
                }
                let popup = PopupDialog(title: message, message: "")
                popup.addButton(PopupDialogButton(title: "ja", action: {
                    
                    update_schicht(oldschicht: schicht, newschicht: Schicht2Change(schicht:schicht,accept_id:accept_id), on_success: {
                        print("OK")
                        sendMessage(to: "\(schicht.user.id)", title: title, body: body)
                        self.init_view(for: user)
                    },on_error: {
                        error in print(error)

                    })
                }))
                self.present(popup,animated: true,completion: nil)
            }

        }else{
            LoginService.callLoginDialog(from: self, completion: {
                user in self.init_view(for: user)
            })
        }
    }

}

class CalendarViewCell:UITableViewCell{
    @IBOutlet weak var lbl_name: UILabel!
    @IBOutlet weak var lbl_type: UILabel!

    @IBOutlet weak var img_acc: UIImageView!
    
}
