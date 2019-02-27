package com.example.dell.cloudreaderapp.ui.fragment

import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.databinding.FragmentWanAndroidBinding
import com.example.dell.cloudreaderapp.viewmodel.menu.NoViewModel

/**
 * Created by chentao
 * Date:2019/2/19
 * Description:
 */
class BannerFragment() : BaseFragment<NoViewModel, FragmentWanAndroidBinding>() {


    companion object {

        fun getInstance(): BannerFragment {
            var fragment = BannerFragment()
            return fragment
        }
    }

    override fun setContent(): Int {
        return R.layout.fragment_wan_android
    }
}