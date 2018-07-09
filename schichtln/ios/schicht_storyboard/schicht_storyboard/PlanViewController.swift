//
//  SecondViewController.swift
//  schicht_storyboard
//
//  Created by Andreas Denk on 09.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit

import ViewPager_Swift

class PlanViewController: UIViewController {

    @IBAction func btn_login_action(_ sender: Any) {
        print("login")
    }
    @IBOutlet weak var btn_login: UIBarButtonItem!
    
    var positions:[Group] = []
    var tabs: [ViewPagerTab] = [ViewPagerTab(title: "", image: nil)
    ]
    
    var viewPager:ViewPagerController!
    var options:ViewPagerOptions!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.edgesForExtendedLayout = UIRectEdge.init(rawValue: 0)
        
        
        options = ViewPagerOptions(viewPagerWithFrame: self.view.bounds)
        options.tabType = ViewPagerTabType.basic
        options.tabViewImageSize = CGSize(width: 20, height: 20)
        options.tabViewTextFont = UIFont.systemFont(ofSize: 16)
        options.tabViewPaddingLeft = 20
        options.tabViewPaddingRight = 20
        options.isTabHighlightAvailable = true
        options.isEachTabEvenlyDistributed = true
        options.fitAllTabsInView = true
        

        
        get_positions_by_user(user: test_user, on_success: { positions in
            self.tabs.removeAll()
            self.positions=positions
            for p in positions{
                self.tabs.append(ViewPagerTab(title: p.name, image: nil))

            }
            self.viewPager = ViewPagerController()
             self.viewPager.options = self.options
             self.viewPager.dataSource = self
             self.viewPager.delegate = self

            self.addChildViewController(self.viewPager)
            self.view.addSubview( self.viewPager.view)
             self.viewPager.didMove(toParentViewController: self)
            self.viewPager.invalidateTabs()
            
        }, on_error: {error in
            
        })
        
        
    }
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        
        options.viewPagerFrame = self.view.bounds
    }
}


extension PlanViewController: ViewPagerControllerDataSource {
    
    func numberOfPages() -> Int {
        return tabs.count
    }
    
    func viewControllerAtPosition(position:Int) -> UIViewController {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "PlanDetailViewController") as! PlanDetailViewController
        vc.position = positions[position]
        return vc
    }
    
    func tabsForPages() -> [ViewPagerTab] {
        return tabs
    }
    
    func startViewPagerAtIndex() -> Int {
        return 0
    }
}

extension PlanViewController: ViewPagerControllerDelegate {
    
    func willMoveToControllerAtIndex(index:Int) {
    }
    
    func didMoveToControllerAtIndex(index: Int) {
    }
}
