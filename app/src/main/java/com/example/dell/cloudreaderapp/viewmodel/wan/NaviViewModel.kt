package com.example.dell.cloudreaderapp.viewmodel.wan

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.dell.cloudreaderapp.bean.wanandroid.NaviJsonBean

/**
 * Created by chentao
 * Date:2019/3/6
 * Description:
 */
class NaviViewModel(application: Application):AndroidViewModel(application) {


    //获取数据
    fun getNaviJson():MutableLiveData<NaviJsonBean>{

        var data:MutableLiveData<NaviJsonBean> =MutableLiveData<NaviJsonBean>()

        return data
    }
}