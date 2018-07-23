//
//  HomeViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 09.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit

import ViewPager_Swift
import PopupDialog

class HomeViewController: UIViewController {
    @IBAction func btn_logout(_ sender: Any) {
        let popup = PopupDialog(title: "really?", message: "really, really?")
        let btn_yep = DefaultButton(title: "yep", dismissOnTap: false){
            popup.dismiss({
                StoredValues.user = nil
                LoginService.callLoginDialog(from: self,
                                             completion: {
                                                user in
                                                self.init_view(for: user)
                })
                }
            )
        }
        popup.addButton(btn_yep)
        self.present(popup,animated: true , completion: nil)
    }

    @IBOutlet weak var btn_logout: UIBarButtonItem!

    var tabs: [ViewPagerTab] = [
        ViewPagerTab(title: "Kalender", image: UIImage(named: "calendar")),
        ViewPagerTab(title: "meine Schichten", image: UIImage(named: "personal")),
        ViewPagerTab(title: "Tauschbörse", image: UIImage(named: "trade"))
    ]

    var viewPager:ViewPagerController!
    var options:ViewPagerOptions!

    override func viewDidLoad() {
        super.viewDidLoad()
        if let user = StoredValues.user{
            init_view(for: user)
        }else{
            LoginService.callLoginDialog(from: self) { (user) in
                self.init_view(for: user)
            }
        }
    }
    fileprivate func init_view(for user :User) {
        btn_logout.title = "\(user.username) ausloggen"

        self.edgesForExtendedLayout = UIRectEdge.init(rawValue: 0)


        options = ViewPagerOptions(viewPagerWithFrame: self.view.bounds)
        options.tabType = ViewPagerTabType.imageWithText
        options.tabViewImageSize = CGSize(width: 20, height: 20)
        options.tabViewTextFont = UIFont.systemFont(ofSize: 16)
        options.tabViewPaddingLeft = 20
        options.tabViewPaddingRight = 20
        options.isTabHighlightAvailable = false
        options.tabIndicatorViewBackgroundColor = UIColor(named: "colorAccent")!
        options.tabViewBackgroundDefaultColor = UIColor(named: "colorPrimaryDark")!
        options.tabViewTextDefaultColor = UIColor(named: "colorPrimary")!
        options.isEachTabEvenlyDistributed = true
        options.fitAllTabsInView = true

        self.viewPager = ViewPagerController()
        self.viewPager.options = self.options
        self.viewPager.dataSource = self
        self.viewPager.delegate = self

        self.addChildViewController(self.viewPager)
        self.view.addSubview( self.viewPager.view)
        self.viewPager.didMove(toParentViewController: self)
        self.viewPager.invalidateTabs()
    }

    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()

        options.viewPagerFrame = self.view.bounds
    }
    
}


extension HomeViewController: ViewPagerControllerDataSource {

    func numberOfPages() -> Int {
        return tabs.count
    }

    func viewControllerAtPosition(position:Int) -> UIViewController {
        switch position {
        case 1:
            return self.storyboard?.instantiateViewController(withIdentifier: "personalViewController") as! PersonalViewController
        case 2:
            return self.storyboard?.instantiateViewController(withIdentifier: "tradeViewController") as! TradeViewController
        default:
            return self.storyboard?.instantiateViewController(withIdentifier: "calendarViewController") as! CalendarViewController

        }


    }

    func tabsForPages() -> [ViewPagerTab] {
        return tabs
    }

    func startViewPagerAtIndex() -> Int {
        return 0
    }
}

extension HomeViewController: ViewPagerControllerDelegate {

    func willMoveToControllerAtIndex(index:Int) {
    }

    func didMoveToControllerAtIndex(index: Int) {
    }


    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

