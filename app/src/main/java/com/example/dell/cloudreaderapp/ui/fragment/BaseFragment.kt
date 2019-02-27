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
import rx.subscriptions.CompositeSubscription

/**
 * Created by chentao
 * Date:2019/2/15
 * Description:基类的Fragment
 */
abstract class BaseFragment<VM : AndroidViewModel, SV : ViewDataBinding> : Fragment() {

    //声明ViewModel
    protected var viewModel: VM? = null
    // 布局view
    protected var bindingView: SV? = null
    // fragment是否显示了
    protected var mIsVisible = false
    // 加载中
    protected var loadingView: View? = null
    // 加载失败
    protected var mRefresh: LinearLayout? = null
    // 内容布局
    protected var mContainer: RelativeLayout? = null
    // 动画
    protected var mAnimationDrawable: AnimationDrawable? = null
    protected var mCompositeSubscription: CompositeSubscription? = null

    private var v:View?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.fragment_base, container, false)
        bindingView = DataBindingUtil.inflate(inflater, setContent(), container, false)
        var params:RelativeLayout.LayoutParams=RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        bindingView?.root?.layoutParams=params
        mContainer=v?.findViewById(R.id.container)
        mContainer?.addView(bindingView?.root)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
    protected fun onRefresh() {

    }

    /**
     * 显示加载中状态
     */
    protected fun showLoading() {

        if (loadingView != null && loadingView?.getVisibility() != View.VISIBLE) {
            loadingView?.setVisibility(View.VISIBLE)
        }
        // 开始动画
        if ((mAnimationDrawable?.isRunning()!=true)) {
            mAnimationDrawable?.start()
        }
        if (bindingView?.getRoot()?.visibility != View.GONE) {
            bindingView?.getRoot()?.visibility = View.GONE
        }
        if (mRefresh?.getVisibility() != View.GONE) {
            mRefresh?.setVisibility(View.GONE)
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

    protected fun onInvisible() {}


    /**
     * 显示时加载数据,需要这样的使用
     * 注意声明 isPrepared，先初始化
     * 生命周期会先执行 setUserVisibleHint 再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected fun loadData() {}

    protected fun onVisible() {
        loadData()
    }

    protected fun <T : View> getView(id: Int): T? {
        var getView=view
        var t:T=getView?.findViewById<View>(id) as T
        return t
    }


}