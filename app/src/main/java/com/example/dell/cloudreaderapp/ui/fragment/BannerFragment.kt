package com.example.dell.cloudreaderapp.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.adapter.WanAndroidAdapter
import com.example.dell.cloudreaderapp.bean.wanandroid.HomeListBean
import com.example.dell.cloudreaderapp.bean.wanandroid.WanAndroidBannerBean
import com.example.dell.cloudreaderapp.databinding.FragmentWanAndroidBinding
import com.example.dell.cloudreaderapp.databinding.HeaderWanAndroidBinding
import com.example.dell.cloudreaderapp.utils.CommonUtils
import com.example.dell.cloudreaderapp.utils.DensityUtil
import com.example.dell.cloudreaderapp.utils.GlideImageLoader
import com.example.dell.cloudreaderapp.utils.ImageLoadUtil
import com.example.dell.cloudreaderapp.viewmodel.menu.NoViewModel
import com.example.dell.cloudreaderapp.viewmodel.wan.WanAndroidListViewModel
import com.example.xrecyclerview.XRecyclerView
import com.tencent.bugly.proguard.t
import com.youth.banner.BannerConfig
import java.util.ArrayList

/**
 * Created by chentao
 * Date:2019/2/19
 * Description: 玩Android的BannerFragment界面
 */
class BannerFragment() : BaseFragment<WanAndroidListViewModel, FragmentWanAndroidBinding>() {


    lateinit var androidBinding: HeaderWanAndroidBinding
    var mIsPrepared: Boolean = false
    var mIsFirst = true
    private var isLoadBanner = false
    private var mAdapter: WanAndroidAdapter? = null

    companion object {

        fun getInstance(): BannerFragment {
            var fragment = BannerFragment()
            return fragment
        }
    }

    override fun setContent(): Int {
        return R.layout.fragment_wan_android
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //显示加载视图
        showContentView()
        initRefreshView()  //初始化RecyclerView视图

        //准备就绪
        mIsPrepared = true
        loadData()
    }

    //加载数据
    override fun loadData() {
        //这3个有一个等于false就终止
        if (!mIsVisible || !mIsPrepared || !mIsFirst) {
            return
        }
        bindingView?.srlWan?.isRefreshing = true //显示加载
        getHomeList()
    }


    //获取列表数据
    fun getHomeList() {

        //ViewModel是被观察者  viewModel发射数据 观察者接收数据
        viewModel?.getHomeList(null)?.observe(this@BannerFragment, object : Observer<HomeListBean> {
            override fun onChanged(homeListBean: HomeListBean?) {

                bindingView?.apply {
                    if (srlWan.isRefreshing) { //隐藏进度加载圈
                        srlWan.isRefreshing = false
                    }
                }
                //如果从后台获取的列表数据不为空
                if (homeListBean != null
                        && homeListBean.getData() != null
                        && homeListBean.getData().getDatas() != null
                        && homeListBean.getData().getDatas().size > 0) {
                    if (viewModel?.getPage() == 0) {
                        showContentView()
                        mAdapter?.clear()
                        mAdapter?.notifyDataSetChanged()
                    }
                    //  一个刷新头布局 一个header
                    var itemCount=mAdapter?.getItemCount()
                    var positionStart=0
                    itemCount?.let {
                        positionStart= itemCount + 2
                    }

                    mAdapter?.addAll(homeListBean.getData().getDatas())
                    mAdapter?.notifyItemRangeInserted(positionStart, homeListBean.getData().getDatas().size)
                    bindingView?.xrvWan?.refreshComplete()  //刷新数据完成

                    if (viewModel?.getPage() === 0) {
                        mIsFirst = false
                    }
                } else {
                    if (viewModel?.getPage() === 0) {
                        showError()
                    } else {
                        bindingView?.xrvWan?.refreshComplete()
                        bindingView?.xrvWan?.noMoreLoading()
                    }
                }
            }
        })



    }


    /**
     * 设置banner图
     */
    fun showBannerView(bannerImages: ArrayList<String>, mBannerTitle: ArrayList<String>, result: List<WanAndroidBannerBean.DataBean>) {
        androidBinding.rlBanner.visibility = View.VISIBLE
        androidBinding.banner.setBannerTitles(mBannerTitle)  //设置banner图的titile
        androidBinding.banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
        androidBinding.banner.setImages(bannerImages).setImageLoader(GlideImageLoader()).start()
        androidBinding.banner.setOnBannerListener { position ->
            if (result[position] != null && !TextUtils.isEmpty(result[position].url)) {
//                WebViewActivity.loadUrl(context, result[position].url, result[position].title)
            }
        }
        val size = bannerImages.size
        var position1 = 0
        var position2 = 0
        if (size > 1) {
            position1 = size - 2
            position2 = size - 1
        }
        ImageLoadUtil.displayFadeImage(androidBinding.ivBannerOne, bannerImages[position1], 3)
        ImageLoadUtil.displayFadeImage(androidBinding.ivBannerTwo, bannerImages[position2], 3)
        val finalPosition = position1
        val finalPosition2 = position2
        androidBinding.ivBannerOne.setOnClickListener { v ->
            {
                //WebViewActivity.loadUrl(context, result[finalPosition].url, result[finalPosition].title)
            }

            androidBinding.ivBannerTwo.setOnClickListener { v ->
                //WebViewActivity.loadUrl(context, result[finalPosition2].url, result[finalPosition2].title)
            }
            isLoadBanner = true
        }
    }

    //初始化RecyclerView
    fun initRefreshView() {
        bindingView?.srlWan?.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme))
        bindingView?.xrvWan?.setLayoutManager(LinearLayoutManager(activity))
        bindingView?.xrvWan?.setPullRefreshEnabled(false)
        bindingView?.xrvWan?.clearHeader()
        bindingView?.xrvWan?.itemAnimator = null
        mAdapter = WanAndroidAdapter(getActivity()) //获取适配器对象
        bindingView?.xrvWan?.adapter = mAdapter
        androidBinding = DataBindingUtil.inflate<HeaderWanAndroidBinding>(getLayoutInflater(), R.layout.header_wan_android, null, false)
        //RecyclerView添加头部布局
        bindingView?.xrvWan?.addHeaderView(androidBinding.root)
        DensityUtil.formatBannerHeight(androidBinding.banner, androidBinding.llBannerImage)

        //获取轮播图
        viewModel?.getWanAndroidBanner()?.observe(this, object : Observer<WanAndroidBannerBean> {
            override fun onChanged(bean: WanAndroidBannerBean?) {

                bindingView?.apply {
                    if (srlWan.isRefreshing) {
                        srlWan.isRefreshing = false
                    }
                    if (bean != null) {
                        showBannerView(bean.getmBannerImages(), bean.getmBannerTitles(), bean.data)
                        androidBinding.rlBanner.visibility = View.VISIBLE
                    } else {
                        androidBinding.rlBanner.visibility = View.GONE
                    }
                }
            }
        })

        //下拉刷新 SwiperRefreshLayout下拉刷新
        bindingView?.srlWan?.setOnRefreshListener(object :SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                Log.i("gdchent","OnRefreshListener_onRefresh")
                swipeRefresh()
            }
        });
        //下拉刷新监听
        bindingView?.xrvWan?.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                Log.i("gdchent","LoadingListener_onRefresh")
                swipeRefresh()
            }

            override fun onLoadMore() {
                viewModel?.apply {
                    var page = getPage()
                    viewModel?.setPage(++page)
                    getHomeList()
                }

            }
        })

    }

    /**
     * RecyclerView的下拉刷新
     */
    private fun swipeRefresh(){
        Log.i("gdchent","下拉刷新开始监听")
        viewModel?.setPage(0)
        bindingView?.xrvWan?.reset()
        getHomeList() //重新获取列表数据
    }

}