package com.llw.mvpdemo;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.llw.mvpdemo.adapter.WallPaperAdapter;
import com.llw.mvpdemo.bean.WallPaperResponse;
import com.llw.mvpdemo.contract.MainContract;
import com.llw.mvplibrary.mvp.MvpActivity;
import com.llw.mvplibrary.network.utils.KLog;
import java.util.ArrayList;
import java.util.List;


/**
 * @author llw
 */
//MvpActivity一定要继承，有了他，就将自己的弱引用传给了BasePresenter(通过实现(IMainView实现的BaseView)来传递)，从而在实用Presenter中调用数据  处理方法(getView.处理方法)(该点靠实现IMainView，回调在本Activity中)
//向MvpActivity指定实用Presenter，其会在MvpActivity中自动创建实例(在本Activity中回调new出来的),从而可以  直接使用MvpActivity中的对象mPresenter来直接调用数据请求方法
public class MainActivity extends MvpActivity<MainContract.MainPresenter> implements MainContract.IMainView {


    private static final String TAG = "MainActivity";
    private final List<WallPaperResponse.ResBean.VerticalBean> mList = new ArrayList<>();
    private WallPaperAdapter mAdapter;

    //getLayoutId是BaseActivity要求重写的，该方法实际是setContentView(R.layout.xxx)
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    //initData是BaseActivity要求重写的，该方法实际执行在onCreate期间
    @Override
    public void initData(Bundle savedInstanceState) {
        //显示加载弹窗
        showLoadingDialog();
        //初始化列表
        initList();
    }

    @Override
    protected MainContract.MainPresenter createPresenter() {
        return new MainContract.MainPresenter();
    }
    /**
     * 初始化列表
     */
    private void initList() {
        RecyclerView rv = findViewById(R.id.rv);
        //配置rv
        mAdapter = new WallPaperAdapter(mList);
        rv.setLayoutManager(new GridLayoutManager(context,2));
        rv.setAdapter(mAdapter);

        //****向Presenter请求数据，请求过程在Contract中的Presenter中，这里只负责发送请求，以及
        //用回调接口写“数据返回后的视图处理“
        mPresenter.getWallPaper();
    }

    /**
     * “数据返回后的视图处理“
     *
     * @param wallPaperResponse
     */
    @Override
    public void getWallPaper(WallPaperResponse wallPaperResponse) {
        List<WallPaperResponse.ResBean.VerticalBean> vertical = wallPaperResponse.getRes().getVertical();
        if (vertical != null && vertical.size() > 0) {
            mList.clear();
            mList.addAll(vertical);
            mAdapter.notifyDataSetChanged();
        } else {
            showMsg("数据为空");
        }
        hideLoadingDialog();
    }

    /**
     * 获取列表数据异常
     *
     * @param e
     */
    @Override
    public void getWallPaperFailed(Throwable e) {
        KLog.e(TAG,e.toString());
        showMsg("获取列表数据异常，具体日志信息请查看日志");
        hideLoadingDialog();
    }




}
