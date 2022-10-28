package com.llw.mvplibrary.mvp;

import android.os.Bundle;

import com.llw.mvplibrary.base.BaseActivity;
import com.llw.mvplibrary.base.BasePresenter;
import com.llw.mvplibrary.base.BaseView;

/**
 * 适用于需要访问网络接口的Activity
 *
 * @author llw
 */
//BaseActivity和MainActivity的中间层，主要是
// 1.向MainActivity索要BasePresenter的实现类，该类具有数据请求方法
// 2.将MainActivity的弱引用传递给BasePresenter
public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {

    protected P mPresenter;

    /**
     * 创建Presenter
     */
    protected abstract P createPresenter();

    //该方法是BaseActivity中onCreate过程调用，在设置视图和初始化视图及请求数据之前
    //1. 用于将实现者MainActivity的弱引用返回给BasePresenter从而给真正的数据请求者Presenter使用
    //2. 用于提供方法让Activity来指定(泛型)实用Presenter，并在MvpActivity中持有，从而直接使用其实例mPresenter来调用数据请求方法
    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        //创建
        mPresenter = createPresenter();
        //绑定View
        mPresenter.attachView((BaseView) this);
    }

    /**
     * 页面销毁时解除绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
