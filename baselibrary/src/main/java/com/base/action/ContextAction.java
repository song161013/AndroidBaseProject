package com.base.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;


public interface ContextAction {

    /**
     * 获取Context
     */
    Context getContext();

    /**
     * 获取资源对象（仅供子类调用）
     */
    default Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 根据 id 获取一个文本
     */
    default String getString(@StringRes int id) {
        return getContext().getString(id);
    }

    /**
     * 根据ID获取Drawable
     */
    default Drawable getDdrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    @ColorInt
    default int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    default void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(getContext(), clazz));
    }

    /**
     * 启动一个activity
     */
    default void startActivity(Intent intent) {
        if (!(getContext() instanceof Activity)) {
            // 如果当前的上下文不是 Activity，调用 startActivity 必须加入新任务栈的标记
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        getContext().startActivity(intent);
    }

    /**
     * 获取 系统服务
     */
    default <S> S getSystemService(Class<S> clazz) {
        return ContextCompat.getSystemService(getContext(), clazz);
    }
}
