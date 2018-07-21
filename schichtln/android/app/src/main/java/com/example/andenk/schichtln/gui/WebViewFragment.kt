package com.example.andenk.schichtln.gui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.example.andenk.schichtln.R
import kotlinx.android.synthetic.main.fragment_webview.view.*


class WebViewFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view :View =inflater!!.inflate(R.layout.fragment_webview, container, false)

        view.view_webview.loadUrl("http://www.cinema-muenchen.de/programm.html")



        return view



    }
}
