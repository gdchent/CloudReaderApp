package com.example.dell.cloudreaderapp.http;

import com.example.dell.cloudreaderapp.bean.FrontpageBean;
import com.example.dell.cloudreaderapp.bean.wanandroid.HomeListBean;
import com.example.dell.cloudreaderapp.bean.wanandroid.TreeBean;
import com.example.dell.cloudreaderapp.bean.wanandroid.WanAndroidBannerBean;
import com.example.http.HttpUtils;
import com.example.http.utils.BuildFactory;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author jingbin
 * @date 16/11/21
 * 网络请求类（一个接口一个方法）
 */
public interface HttpClient {

    class Builder {
        public static HttpClient getDouBanService() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_DOUBAN);
        }

        public static HttpClient getTingServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_TING);
        }

        public static HttpClient getGankIOServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_GANKIO);
        }

        public static HttpClient getFirServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_FIR);
        }

        public static HttpClient getWanAndroidServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_WAN_ANDROID);
        }

        public static HttpClient getQSBKServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_QSBK);
        }
    }

    /**
     * 首页轮播图
     */
    @GET("ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14")
    Observable<FrontpageBean> getFrontpage();

    /**
     * 玩安卓轮播图
     */
    @GET("banner/json")
    Observable<WanAndroidBannerBean> getWanAndroidBanner();


    /**
     * 玩安卓，文章列表、知识体系下的文章
     *
     * @param page 页码，从0开始
     * @param cid  体系id
     */
    @GET("article/list/{page}/json")
    Observable<HomeListBean> getHomeList(@Path("page") int page, @Query("cid") Integer cid);
}