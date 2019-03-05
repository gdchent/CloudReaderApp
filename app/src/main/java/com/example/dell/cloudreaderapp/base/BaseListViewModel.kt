package com.example.dell.cloudreaderapp.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

/**
 * Created by chentao
 * Date:2019/3/5
 * Description:
 */
open class BaseListViewModel(application: Application):AndroidViewModel(application) {

    var mPage:Int=0


    fun getPage(): Int {
        return mPage
    }

    fun setPage(mPage: Int) {
        this.mPage = mPage
    }
}