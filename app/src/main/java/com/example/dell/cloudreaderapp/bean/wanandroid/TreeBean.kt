package com.example.dell.cloudreaderapp.bean.wanandroid

import android.databinding.BaseObservable

/**
 * Created by chentao
 * Date:2019/2/27
 * Description:
 */
class TreeBean():BaseObservable() {

    private var errorCode:Int=0
    private var errorMsg:String?=null
    private var data:List<DataBean>?=null

    companion object {
        class DataBean{

        }
    }
}