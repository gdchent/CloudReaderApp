package com.example.dell.cloudreaderapp.ui.act

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.dell.cloudreaderapp.R
import com.example.dell.cloudreaderapp.app.ConstantsImageUrl
import com.example.dell.cloudreaderapp.databinding.ActivityMainBinding
import com.example.dell.cloudreaderapp.databinding.NavHeaderMainBinding
import com.example.dell.cloudreaderapp.rx.RxBus
import com.example.dell.cloudreaderapp.rx.RxBusBaseMessage
import com.example.dell.cloudreaderapp.rx.RxCodeConstants
import com.example.dell.cloudreaderapp.statusbar.StatusBarUtil
import com.example.dell.cloudreaderapp.ui.fragment.MyBottomSheetDialogFragment
import com.example.dell.cloudreaderapp.utils.*
import rx.Subscription
import rx.subscriptions.CompositeSubscription


class MainActivity : AppCompatActivity(),View.OnClickListener {


    private var llTitleMenu: FrameLayout? = null
    private var toolbar: Toolbar? = null
    private var navView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var vpContent: ViewPager? = null
    private var mBinding: ActivityMainBinding? = null
    private var ivTitleTwo: ImageView? = null
    private var ivTitleOne: ImageView? = null
    private var ivTitleThree: ImageView? = null
    private var mCompositeSubscription: CompositeSubscription? = null
    private var bind: NavHeaderMainBinding? = null
    private var tvStartBottomDialog:TextView?=null

    private var myBottomSheetDialogFragment: MyBottomSheetDialogFragment?=null
    companion object {
        var isLaunch: Boolean = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        isLaunch = true
        initStatusView()
        initId()
        initRxBus()

        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this@MainActivity, drawerLayout,
                CommonUtils.getColor(R.color.colorTheme))
        initDrawerLayout()
        initListener()
        window.decorView.rootView
        //StringTest.test("adbc","abc")
    }

    //初始化status的View
    fun initStatusView() {
        mBinding?.let {
            val layoutParams = it.include.viewStatus.getLayoutParams()
            layoutParams.height = StatusBarUtil.getStatusBarHeight(this)
            it.include.viewStatus.setLayoutParams(layoutParams)
        }
    }

    //初始化id
    private fun initId() {
        drawerLayout = mBinding?.drawerLayout
        navView = mBinding?.navView
        toolbar = mBinding?.include?.toolbar
        llTitleMenu = mBinding?.include?.llTitleMenu
        vpContent = mBinding?.include?.vpContent
        ivTitleOne = mBinding?.include?.ivTitleOne
        ivTitleTwo = mBinding?.include?.ivTitleTwo
        ivTitleThree = mBinding?.include?.ivTitleThree
        tvStartBottomDialog=mBinding?.include?.tvStartBottomDialog
    }

    //返回键
    override fun onBackPressed() {
        drawerLayout?.let {
            if (it.isDrawerOpen(Gravity.END)) {
                it.closeDrawer(Gravity.END)
            } else {
                super.onBackPressed()
            }
        }
    }

    fun initRxBus(){
        val subscribe = RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE_TO_ONE, RxBusBaseMessage::class.java)
                .subscribe({ Integer -> setCurrentItem(2) })
        addSubscription(subscribe)
    }


    fun addSubscription(s: Subscription) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = CompositeSubscription()
        }
        this.mCompositeSubscription?.add(s)
    }

    //初始化监听
    fun initListener(){
        llTitleMenu?.setOnClickListener(this)
        tvStartBottomDialog?.setOnClickListener(this)
        mBinding?.include?.ivTitleOne?.setOnClickListener(this)
        mBinding?.include?.ivTitleTwo?.setOnClickListener(this)
        mBinding?.include?.ivTitleThree?.setOnClickListener(this)
    }

    /**
     * inflateHeaderView 进来的布局要宽一些
     */
    private fun initDrawerLayout() {
        navView?.inflateHeaderView(R.layout.nav_header_main)
        val headerView = navView?.getHeaderView(0)
        headerView?.apply {
            bind = DataBindingUtil.bind<NavHeaderMainBinding>(this)
        }
        bind?.setListener(this)
        bind?.dayNightSwitch?.setChecked(SPUtils.getNightMode())

        ImageLoadUtil.displayCircle(bind?.ivAvatar, ConstantsImageUrl.IC_AVATAR)
        bind?.llNavExit?.setOnClickListener(this)
        bind?.ivAvatar?.setOnClickListener(this)

        bind?.llNavHomepage?.setOnClickListener(listener)
        bind?.llNavScanDownload?.setOnClickListener(listener)
        bind?.llNavDeedback?.setOnClickListener(listener)
        bind?.llNavAbout?.setOnClickListener(listener)
        bind?.llNavLogin?.setOnClickListener(listener)
        bind?.llNavCollect?.setOnClickListener(listener)

    }


    private val listener = object : PerfectClickListener() {
        override fun onNoDoubleClick(v: View?) {
            mBinding?.drawerLayout?.closeDrawer(GravityCompat.START)
            mBinding?.drawerLayout?.postDelayed({
                when (v?.id) {
//                    R.id.ll_nav_homepage// 主页
//                    -> NavHomePageActivity.startHome(this@MainActivity)
//                    R.id.ll_nav_scan_download//扫码下载
//                    -> NavDownloadActivity.start(this@MainActivity)
//                    R.id.ll_nav_deedback// 问题反馈
//                    -> NavDeedBackActivity.start(this@MainActivity)
//                    R.id.ll_nav_about// 关于云阅
//                    -> NavAboutActivity.start(this@MainActivity)
//                    R.id.ll_nav_collect// 玩安卓收藏
//                    -> if (UserUtil.isLogin(this@MainActivity)) {
//                        MyCollectActivity.start(this@MainActivity)
//                    }
//                    R.id.ll_nav_login// 玩安卓登录
//                    -> DialogBuild.showItems(v, object : OnLoginListener() {
//                        fun loginWanAndroid() {
//                            LoginActivity.start(this@MainActivity)
//                        }
//
//                        fun loginGitHub() {
//                            WebViewActivity.loadUrl(v.context, "https://github.com/login", "登录GitHub账号")
//                        }
//                    })
//                    else -> {
//                    }
                }
            }, 260)
        }
    }

    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private fun setCurrentItem(position: Int) {
        var isOne = false
        var isTwo = false
        var isThree = false
        when (position) {
            0 -> isOne = true
            1 -> isTwo = true
            2 -> isThree = true
            else -> isTwo = true
        }
        vpContent?.setCurrentItem(position)
        ivTitleOne?.setSelected(isOne)
        ivTitleTwo?.setSelected(isTwo)
        ivTitleThree?.setSelected(isThree)
    }

    fun onNightModeClick(view:View){

    }

    fun getNightMode(){

    }

    //点击事件
    override fun onClick(v: View?) {
            when(v?.id){
                 R.id.ll_title_menu ->{ //菜单点击事件
                       Log.i("gdchent","onclick")
                       //如果用户点击的是开启菜单
                     drawerLayout?.openDrawer(GravityCompat.START)  //从左边打开

                 }
                R.id.iv_title_one ->{ //图片1的点击触发 走什么逻辑
                    Log.i("gdchent","one")
                }
                R.id.iv_title_two -> {  //图片2点击触发逻辑
                    Log.i("gdchent","two")
                }
                R.id.iv_title_three ->{ //图片3点击触发逻辑
                    Log.i("gdchent","three")
                }
                R.id.tv_start_bottom_dialog ->{
                    Log.i("gdchent","bottom")
                    if (myBottomSheetDialogFragment==null){
                        myBottomSheetDialogFragment= MyBottomSheetDialogFragment()

                    }
                    myBottomSheetDialogFragment?.show(supportFragmentManager,"dialog")

                }
            }
    }
}
