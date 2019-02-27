package com.example.dell.cloudreaderapp.ui.fragment

import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.databinding.FragmentGankBinding
import com.example.dell.cloudreaderapp.viewmodel.menu.NoViewModel

/**
 * Created by chentao
 * Date:2019/2/19
 * Description:
 */
class DoubanFragment():BaseFragment<NoViewModel,FragmentGankBinding>() {

    override fun setContent(): Int {
        return R.layout.fragment_gank
    }
}