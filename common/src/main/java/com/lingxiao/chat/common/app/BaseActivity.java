package com.lingxiao.chat.common.app;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用的初始化窗口
        initWindows();
        if (initArgs(getIntent().getExtras())){
            int layID= getContentLayoutId();
            setContentView(layID);
            initWidget();
            initData();
        }else {
            finish();
        }
    }

    /**
     *初始化窗口
     */
    protected void initWindows(){

    }

    /**
     * 初始化相关参数
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 得到当前界面的资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    /**
     *  初始化数据
     */
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前activity下所有的fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0){
            for (Fragment fragment: fragments) {
                //如果是继承我自己的base
                if (fragment instanceof BaseFragment){
                    if (((BaseFragment) fragment).onBackPressed()){
                        //判断是否拦截了返回按钮
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
