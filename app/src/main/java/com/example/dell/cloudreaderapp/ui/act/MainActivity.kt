package com.example.dell.cloudreaderapp.ui.act

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
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
import com.example.dell.cloudreaderapp.ui.fragment.DoubanFragment
import com.example.dell.cloudreaderapp.ui.fragment.GankFragment
import com.example.dell.cloudreaderapp.ui.fragment.MyBottomSheetDialogFragment
import com.example.dell.cloudreaderapp.ui.fragment.WanFragment
import com.example.dell.cloudreaderapp.utils.*
import com.example.dell.cloudreaderapp.view.MyFragmentPagerAdapter
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * 主页面 使用的是侧边栏  drawerlayout 布局
 *  主布局使用的是ToolBar +ViewPager的布局
 *  思路分析： toolBar上面3个按钮 点击切换viewPager页面  使用vp.setCurrentItem(0)
 */
class MainActivity : AppCompatActivity(),View.OnClickListener,ViewPager.OnPageChangeListener {


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
        initContentFragment()  //初始化ViewPager对应的适配器的内容区域
        initDrawerLayout()
        initListener()

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

    //初始化内容Fragment
    fun initContentFragment(){
        var fragmentList:ArrayList<Fragment> = ArrayList()
        fragmentList.add(WanFragment())  //viewapger对应的第一个Fragment
        fragmentList.add(GankFragment()) //viewpager对应的第二个Fragment
        fragmentList.add(DoubanFragment()) //viewpager对应的第三个Fragment

        //获取适配器
        var mAdapter:MyFragmentPagerAdapter= MyFragmentPagerAdapter(supportFragmentManager,fragmentList)
        vpContent?.apply {
            adapter=mAdapter
        }
        vpContent?.offscreenPageLimit=2  //这个不设置会出问题的
        toolbar?.setTitle("") //设置标题栏木为空
        setSupportActionBar(toolbar)

        //默认设置第0页
        vpContent?.setCurrentItem(0)
    }

    //初始化id
    private fun initId() {
        drawerLayout = mBinding?.drawerLayout
        navView = mBinding?.navView
        toolbar = mBinding?.include?.toolbar    //最外面的toolbar
        llTitleMenu = mBinding?.include?.llTitleMenu   //点击拉出侧拉菜单的按钮
        vpContent = mBinding?.include?.vpContent   //viewpager
        ivTitleOne = mBinding?.include?.ivTitleOne   //图标1
        ivTitleTwo = mBinding?.include?.ivTitleTwo  //图标2
        ivTitleThree = mBinding?.include?.ivTitleThree  //图标3
        tvStartBottomDialog=mBinding?.include?.tvStartBottomDialog
    }

    //返回键
    override fun onBackPressed() {
        drawerLayout?.let {
            if (it.isDrawerOpen(Gravity.START)) {
                it.closeDrawer(Gravity.START)
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
        llTitleMenu?.setOnClickListener(this)  //拉出侧拉菜单的点击监听设置
        tvStartBottomDialog?.setOnClickListener(this)

        /**
         * toolBar区域最上面点击事件
         */
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
                R.id.iv_title_one ->{ //图片1的点击触发  设置viewpager的当前页面为1
                    //Log.i("gdchent","one")
                    //加个判断  如果是在当前页面
                    if(vpContent?.currentItem!=0){
                        setCurrentItem(0)
                    }

                }
                R.id.iv_title_two -> {  //图片2点击触发逻辑 设置viewpager的当前页面为2
                    //Log.i("gdchent","two")
                    if(vpContent?.currentItem!=1){
                        setCurrentItem(1)
                    }

                }
                R.id.iv_title_three ->{ //图片3点击触发逻辑 设置viewpager当前页面为3
                    //Log.i("gdchent","three")
                    if(vpContent?.currentItem!=2){
                        setCurrentItem(2)
                    }

                }
                R.id.tv_start_bottom_dialog ->{
                    //Log.i("gdchent","bottom")
                    if (myBottomSheetDialogFragment==null){
                        myBottomSheetDialogFragment= MyBottomSheetDialogFragment()

                    }
                    myBottomSheetDialogFragment?.show(supportFragmentManager,"dialog")

                }
            }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        when(position){
            0 ->{
                setCurrentItem(position)
            }
            1 ->{
                setCurrentItem(position)
            }
            2 ->{
                setCurrentItem(position)
            }
            else ->{
                setCurrentItem(0)
            }
        }
    }
}
