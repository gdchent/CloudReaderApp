package com.example.dell.cloudreaderapp.ui.fragment

import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.databinding.FragmentNaviBinding
import com.example.dell.cloudreaderapp.viewmodel.wan.NaviViewModel

/**
 * Created by chentao
 * Date:2019/3/6
 * Description:
 */
class NaviFragment():BaseFragment<NaviViewModel,FragmentNaviBinding>() {


     companion object {
         fun getInstance():NaviFragment{
             var fragment=NaviFragment()
             return fragment
         }
     }
    override fun setContent(): Int {
       return R.layout.fragment_navi
    }
}