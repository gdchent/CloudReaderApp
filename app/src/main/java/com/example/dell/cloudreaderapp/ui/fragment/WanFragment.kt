package com.example.dell.cloudreaderapp.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.databinding.FragmentBookBinding
import com.example.dell.cloudreaderapp.view.MyFragmentPagerAdapter
import com.example.dell.cloudreaderapp.viewmodel.menu.NoViewModel
/**
 * Created by chentao
 * Date:2019/2/15
 * Description:
 */
class WanFragment():BaseFragment<NoViewModel,FragmentBookBinding>() {


    /**
     * 布局是tablayout+viewPager的组合
     */
    //本地定义list的标题tab数据集合
    private var mTitleList=ArrayList<String>()
    private var mFragments = ArrayList<Fragment>(3)
    override fun setContent(): Int {
       return R.layout.fragment_book
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //显示加载进度
        showLoading()
        //初始化FragmentList
        initFragmentList()
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        var fm: FragmentManager =childFragmentManager
        val myAdapter = MyFragmentPagerAdapter(fm, mFragments, mTitleList)
        bindingView?.vpBook?.adapter = myAdapter
        // 左右预加载页面的个数
        bindingView?.vpBook?.offscreenPageLimit = 2
        myAdapter.notifyDataSetChanged()
        bindingView?.tabBook?.tabMode = TabLayout.MODE_FIXED
        bindingView?.tabBook?.setupWithViewPager(bindingView?.vpBook)
    }

    private fun initFragmentList(){
         mTitleList.add("玩安卓")
         mTitleList.add("知识体系")
         mTitleList.add("导航数据")
    }
}