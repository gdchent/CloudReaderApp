package com.example.dell.cloudreaderapp.ui.fragment


import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStore
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.utils.ClassUtil
import com.tencent.bugly.proguard.v
import rx.subscriptions.CompositeSubscription

/**
 * Created by chentao
 * Date:2019/2/15
 * Description:基类的Fragment
 */
abstract class BaseFragment<VM : AndroidViewModel, SV : ViewDataBinding> : Fragment() {


    // ViewModel
    protected var viewModel: VM?=null
    // 布局view
    protected var bindingView: SV?=null
    // fragment是否显示了
    protected var mIsVisible = false
    // 加载中
    private var loadingView: View? = null
    // 加载失败
    private var mRefresh: LinearLayout? = null
    // 内容布局
    protected var mContainer: RelativeLayout?=null
    // 动画
    private var mAnimationDrawable: AnimationDrawable? = null
    private var mCompositeSubscription: CompositeSubscription? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //inflater解析fragment_base这个布局
        var v = inflater.inflate(R.layout.fragment_base, container, false)//获取RelativeLayout布局
        bindingView = DataBindingUtil.inflate(inflater, setContent(), container, false)  //使用databinding获取子类布局
        var params:RelativeLayout.LayoutParams=RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        bindingView?.getRoot()?.layoutParams=params
        mContainer=v?.findViewById(R.id.container) //获取父类的RelativieLayout布局
        mContainer?.addView(bindingView?.getRoot())
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //在基类 加载loadingView
        loadingView =  getView<ViewStub>(R.id.vs_loading)?.inflate()
        val img = loadingView?.findViewById<ImageView>(R.id.img_progress)
        // 加载动画
        mAnimationDrawable = img?.drawable as AnimationDrawable
        // 默认进入页面就开启动画
        if (mAnimationDrawable?.isRunning()!=true) {
            mAnimationDrawable?.start()
        }
        mRefresh = getView(R.id.ll_error_refresh)
        // 点击加载失败布局
        mRefresh?.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                showLoading()
                onRefresh()
            }
        })
        //bindingview这里是加载子类的R.layout.fragment_xxx子布局 返回的是view
        bindingView?.getRoot()?.visibility = View.GONE
        initViewModel()
    }


    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        //可以为空
        val viewModelClass:Class<VM>? = ClassUtil.getViewModel(this@BaseFragment)
        if (viewModelClass != null) {

            var viewModelStore=ViewModelStore()
            //拿到ViewModelProvider
            activity?.let {
                val viewModelProvider = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory(it.application))
                this.viewModel=viewModelProvider.get(viewModelClass)
            }
        }
    }

    /**
     * 布局
     */
    abstract fun setContent(): Int

    /**
     * 加载失败后点击后的操作
     */
   open protected fun onRefresh() {

    }

    /**
     * 显示加载中状态
     */
   open protected fun showLoading() {

        //如果加载视图不为空 并且是可见状体 就隐藏
        if (loadingView != null && loadingView?.getVisibility() != View.VISIBLE) {
            loadingView?.setVisibility(View.VISIBLE)
        }
        // 如果动画不是运行状态 就开始动画
        if ((mAnimationDrawable?.isRunning()!=true)) {
            mAnimationDrawable?.start()
        }
        //
        if (bindingView?.getRoot()?.visibility != View.GONE) {
            bindingView?.getRoot()?.visibility = View.GONE
        }
        if (mRefresh?.getVisibility() != View.GONE) {
            mRefresh?.setVisibility(View.GONE)
        }
    }

    /**
     * 加载完成的状态
     */
   open protected fun showContentView() {
        if (loadingView != null && loadingView?.getVisibility() != View.GONE) {
            loadingView?.setVisibility(View.GONE)
        }
        // 停止动画
        if (mAnimationDrawable?.isRunning()!=true) {
            mAnimationDrawable?.stop()
        }
        if (mRefresh?.getVisibility() != View.GONE) {
            mRefresh?.setVisibility(View.GONE)
        }
        if (bindingView?.getRoot()?.visibility != View.VISIBLE) {
            bindingView?.getRoot()?.visibility = View.VISIBLE
        }
    }


    /**
     * 加载失败点击重新加载的状态
     */
    protected fun showError() {
        if (loadingView != null && loadingView?.getVisibility() != View.GONE) {
            loadingView?.setVisibility(View.GONE)
        }
        // 停止动画
        if (mAnimationDrawable?.isRunning()==true) {
            mAnimationDrawable?.stop()
        }
        if (mRefresh?.getVisibility() != View.VISIBLE) {
            mRefresh?.setVisibility(View.VISIBLE)
        }
        if (bindingView?.getRoot()?.visibility != View.GONE) {
            bindingView?.getRoot()?.visibility = View.GONE
        }
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            mIsVisible = true
            onVisible()
        } else {
            mIsVisible = false
            onInvisible()
        }
    }

   open protected fun onInvisible() {}


    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
   open protected fun loadData() {}

    protected fun onVisible() {
        loadData()
    }

   open protected fun <T : View> getView(id: Int): T? {
        var getView=view
        var t:T=getView?.findViewById<View>(id) as T
        return t
    }


}