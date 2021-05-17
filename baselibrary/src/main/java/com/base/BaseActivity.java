package com.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.base.action.BundleAction;
import com.base.action.ClickAction;
import com.base.action.HandlerAction;

import java.util.Random;

public abstract class BaseActivity extends AppCompatActivity implements BundleAction, ClickAction, HandlerAction {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    protected void initActivity() {
        initLayout();
        initView();
        initData();
    }

    /**
     * 获取布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化布局文件
     */
    protected void initLayout() {
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            initSoftKeybord();
        }
    }

    /**
     * 初始化软键盘
     */
    protected void initSoftKeybord() {
        //TODO 当点击键盘外部时，隐藏键盘
        getContent().setOnClickListener(v -> hideSoftKeyboard());
    }


    @Nullable
    @Override
    public Bundle getBundle() {
        return getIntent().getExtras();
    }

    /**
     * activity被复用时会回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent);
    }

    protected Activity getActivity() {
        return this;
    }

    protected ViewGroup getContent() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }

    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    protected void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    protected OnActivityResultCallBack mCallBack;
    protected int mActivityRequestCode;

    public void startActivityForResult(Class<? extends Activity> clazz, OnActivityResultCallBack callBack) {
        startActivityForResult(new Intent(this, clazz), callBack);
    }

    public void startActivityForResult(Intent intent, OnActivityResultCallBack callBack) {
        startActivityForResult(intent, null, callBack);
    }

    public void startActivityForResult(Intent intent, @Nullable Bundle options, OnActivityResultCallBack callback) {
        // 回调还没有结束，所以不能再次调用此方法，这个方法只适合一对一回调，其他需求请使用原生的方法实现
        if (mCallBack == null) {
            mCallBack = callback;
            // 随机生成请求码，这个请求码必须在 2 的 16 次幂以内，也就是 0 - 65535
            mActivityRequestCode = new Random().nextInt((int) Math.pow(2, 16));
            startActivityForResult(intent, mActivityRequestCode, options);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        hideSoftKeyboard();
        // 查看源码得知 startActivity 最终也会调用 startActivityForResult
        super.startActivityForResult(intent, requestCode, options);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mCallBack != null && mActivityRequestCode == requestCode) {
            mCallBack.onActivityResult(resultCode, data);
            mCallBack = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface OnActivityResultCallBack {

        void onActivityResult(int resultCode, @Nullable Intent data);
    }

    @Override
    protected void onDestroy() {
        removeCallbacks();
        super.onDestroy();
    }

    @Override
    public void finish() {
        hideSoftKeyboard();
        super.finish();
    }
}
