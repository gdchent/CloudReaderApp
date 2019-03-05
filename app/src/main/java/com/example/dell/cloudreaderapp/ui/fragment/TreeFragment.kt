package com.example.dell.cloudreaderapp.ui.fragment

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.adapter.TreeAdapter
import com.example.dell.cloudreaderapp.databinding.FragmentWanAndroidBinding
import com.example.dell.cloudreaderapp.viewmodel.wan.TreeViewModel

/**
 * Created by chentao
 * Date:2019/2/27
 * Description:
 */
class TreeFragment():BaseFragment<TreeViewModel,FragmentWanAndroidBinding>() {

    private var mIsPrepared:Boolean=false;
    private var mIsFirst:Boolean=true
    private var mTreeAdapter:TreeAdapter?=null
    private var fragmentActivity:FragmentActivity ?= null
    override fun setContent(): Int {
        return R.layout.fragment_wan_android
    }
    companion object {

        fun getInstance():TreeFragment{
             var treeFragment=TreeFragment()
             return treeFragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showContentView()
    }


}