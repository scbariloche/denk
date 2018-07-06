//
//  BaseViewController.swift
//  schichtln
//
//  Created by Andreas Denk on 06.07.18.
//  Copyright © 2018 Andreas Denk. All rights reserved.
//

import UIKit
import ViewPager_Swift


class HomeViewController: UIViewController {
    
    let tabs1 = [
        ViewPagerTab(title: "Apple", image:nil),
        ViewPagerTab(title: "Carrot", image:nil)
    ]
    
    let tabs2 = [
        ViewPagerTab(title: "Cheese", image:nil)
    ]
    
    var tabs = [
        ViewPagerTab(title: "Fries", image:nil),
        ViewPagerTab(title: "Apple", image:nil),
        ViewPagerTab(title: "Carrot", image:nil)
    ]
    
    var viewPager:ViewPagerController!
    var options:ViewPagerOptions!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        var user = User()
        user.id=11
        get_all_schichten_by_user(user: user, on_success: {schichts in print(schichts)}, on_error: {error  in print(error.rawValue)})
        self.edgesForExtendedLayout = UIRectEdge.init(rawValue: 0)
        
        self.title = "Awesome View pager"
        
        options = ViewPagerOptions(viewPagerWithFrame: self.view.bounds)
        options.tabType = ViewPagerTabType.basic
        options.tabViewImageSize = CGSize(width: 20, height: 20)
        options.tabViewTextFont = UIFont.systemFont(ofSize: 16)
        options.tabViewPaddingLeft = 20
        options.tabViewPaddingRight = 20
        options.isTabHighlightAvailable = true
        
        viewPager = ViewPagerController()
        viewPager.options = options
        viewPager.dataSource = self
        viewPager.delegate = self
        
        self.addChildViewController(viewPager)
        self.view.addSubview(viewPager.view)
        viewPager.didMove(toParentViewController: self)
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
//        let vc = self.storyboard?.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
//        vc.itemText = "\(tabs[position].title!)"
        
        var vc = UIViewController()
        switch position {
        case 0:
            vc.view.backgroundColor = UIColor.red
        case 1:
            vc.view.backgroundColor = UIColor.blue
        case 2:
            vc.view.backgroundColor = UIColor.brown
        default:
            vc.view.backgroundColor = UIColor.white
        }
        
        return vc
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
        print("Moving to page \(index)")
    }
    
    func didMoveToControllerAtIndex(index: Int) {
        print("Moved to page \(index)")
    }
}
