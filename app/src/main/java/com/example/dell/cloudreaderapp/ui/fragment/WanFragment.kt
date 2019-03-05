package com.example.dell.cloudreaderapp.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.databinding.FragmentBookBinding
import com.example.dell.cloudreaderapp.view.MyFragmentPagerAdapter
import com.example.dell.cloudreaderapp.viewmodel.menu.NoViewModel

/**
 * Created by chentao
 * Date:2019/2/15
 * Description:首页玩安卓
 */
class WanFragment() : BaseFragment<NoViewModel, FragmentBookBinding>() {


    /**
     * 布局是tablayout+viewPager的组合
     */
    //本地定义list的标题tab数据集合
    private var mTitleList = ArrayList<String>(3)
    private var mFragments = ArrayList<Fragment>(3)
    override fun setContent(): Int {
        return R.layout.fragment_book
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.i("gdchent","WanFragment")
        //显示加载进度
        showLoading()
        //初始化FragmentList
        initFragmentList()
        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */

        var myAdapter = MyFragmentPagerAdapter(childFragmentManager, mFragments, mTitleList) //这个适配器是重新创建对象肯定没事
        bindingView?.vpBook?.adapter = myAdapter
        // 左右预加载页面的个数
        bindingView?.vpBook?.offscreenPageLimit = 2
        myAdapter.notifyDataSetChanged()
        bindingView?.tabBook?.tabMode = TabLayout.MODE_FIXED
        bindingView?.tabBook?.setupWithViewPager(bindingView?.vpBook)

        //显示视图
        showContentView()
    }



    private fun initFragmentList() {
        mTitleList.clear()
        mTitleList.add("玩安卓")
        mTitleList.add("知识体系")
        mTitleList.add("导航数据")
        //mFragments.clear()
        mFragments.add(BannerFragment.getInstance()) //WanAndroid对应的第一个Fragment
        mFragments.add(TreeFragment.getInstance())   //WanAndroid对应的第二个Fragment
        mFragments.add(BannerFragment.getInstance())  //WanAndroid对应的第三个Fragment

    }
}