package com.example.dell.cloudreaderapp.viewmodel.wan

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.dell.cloudreaderapp.base.BaseListViewModel
import com.example.dell.cloudreaderapp.bean.wanandroid.HomeListBean
import com.example.dell.cloudreaderapp.bean.wanandroid.WanAndroidBannerBean
import com.example.dell.cloudreaderapp.http.HttpClient
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by chentao
 * Date:2019/3/5
 * Description:
 */
open class WanAndroidListViewModel(application: Application):BaseListViewModel(application) {

    //获取MutableLiveData数据
    open fun getWanAndroidBanner():MutableLiveData<WanAndroidBannerBean>{
        var data=MutableLiveData<WanAndroidBannerBean>()
        HttpClient.Builder.getWanAndroidServer().wanAndroidBanner
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<WanAndroidBannerBean>{
                    override fun onError(e: Throwable?) {

                    }
                    override fun onNext(wanAndroidBannerBean: WanAndroidBannerBean?) {

                        var mBannerImages=ArrayList<String>()
                        var mBannerTitles=ArrayList<String>()
                        if(wanAndroidBannerBean!=null&&wanAndroidBannerBean.data!=null&&wanAndroidBannerBean.data.size>0){

                            val result = wanAndroidBannerBean.data
                            if (result != null && result.size > 0) {
                                for (i in result.indices) {
                                    //获取所有图片
                                    mBannerImages.add(result[i].imagePath)
                                    mBannerTitles.add(result[i].title)
                                }
                                wanAndroidBannerBean.setmBannerImages(mBannerImages)
                                wanAndroidBannerBean.setmBannerTitles(mBannerTitles)
                                data.value = wanAndroidBannerBean
                            }
                        }else{
                             data.value=null
                        }
                    }

                    override fun onCompleted() {

                    }
                })

        return data
    }


    /**
     * @param cid 体系id
     */
    fun getHomeList(cid: Int?): MutableLiveData<HomeListBean> {
        val listData = MutableLiveData<HomeListBean>()
        HttpClient.Builder.getWanAndroidServer().getHomeList(mPage, cid)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<HomeListBean> {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        if (mPage > 0) {
                            mPage--
                        }
                        listData.setValue(null)
                    }

                    override fun onNext(bean: HomeListBean?) {

                        if (bean == null
                                || bean.getData() == null
                                || bean.getData().getDatas() == null
                                || bean.getData().getDatas().size <= 0) {
                            listData.setValue(null)
                        } else {
                            listData.setValue(bean)
                        }
                    }
                })
        return listData
    }

}