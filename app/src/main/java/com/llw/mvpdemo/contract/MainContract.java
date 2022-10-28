package com.llw.mvpdemo.contract;

import android.annotation.SuppressLint;

import com.llw.mvpdemo.api.ApiService;
import com.llw.mvpdemo.bean.WallPaperResponse;
import com.llw.mvplibrary.base.BasePresenter;
import com.llw.mvplibrary.base.BaseView;
import com.llw.mvplibrary.network.NetworkApi;
import com.llw.mvplibrary.network.observer.BaseObserver;

/**
 * 将V与M订阅起来
 * @author llw
 */
//该类主要用于包含BasePresenter的子类，该子类用于数据请求，请求结果用(实现了（实现了BasePresenter的MvpActivity)的MainActivity)的getView()获取MainActivity弱引用
// 来调用实现了（处于BaseView和MainActivity中间的IMainView）中的数据获取后的视图处理方法
    //每个需要不同数据请求的activity都要有相应的数据请求Presenter，而每个Presenter是可以重复利用的
    //使用一个Presenter（即继承MvpActivity时在泛型中传入），就意味着会用到Presenter中提供的数据请求方法，相应的，数据结果和视图呈现的方法也要回调实现（implement Contract.Iview（extends BaseView））
    //BaseView的作用是代表一个Activity已经将自己的弱引用绑定到了BasePresenter中，在该实用的Presenter中可以直接使用BasePresenter的getView获取对象的引用
    //这里的IMainView代表一个实现了BaseView的Activity也实现了数据结果与视图呈现方法，可以直接使用呈现方法
public class MainContract {

    public static class MainPresenter extends BasePresenter<IMainView> {

        @SuppressLint("CheckResult")
        public void getWallPaper(){
            ApiService service  = NetworkApi.createService(ApiService.class);
            service.getWallPaper().compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
                @Override
                public void onSuccess(WallPaperResponse wallPaperResponse) {
                    if (getView() != null) {
                        getView().getWallPaper(wallPaperResponse);
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    if (getView() != null) {
                        getView().getWallPaperFailed(e);
                    }
                }
            }));
        }
    }

    public interface IMainView extends BaseView {
        void getWallPaper(WallPaperResponse wallPaperResponse);
        //获取列表失败返回
        void getWallPaperFailed(Throwable e);
    }
}
