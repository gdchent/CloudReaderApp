package com.example.dell.cloudreaderapp.ui.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dell.cloudreaderapp.R

/**
 * Created by chentao
 * Date:2019/2/13
 * Description:底部弹窗的Fragment
 */
class MyBottomSheetDialogFragment():BottomSheetDialogFragment() {

    var bottomView:View?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomView=inflater.inflate(R.layout.dialog_bottom_view,container,false)
        return bottomView
    }
}