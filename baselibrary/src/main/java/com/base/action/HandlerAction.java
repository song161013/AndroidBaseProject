package com.base.action;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;


public interface HandlerAction {

    Handler HANDLER = new Handler(Looper.getMainLooper());

    default Handler getHandler() {
        return HANDLER;
    }

    default void post(Runnable r) {
        postDelayed(r, 0);
    }

    /**
     * 延迟执行
     *
     * @param r
     * @param delayMillis
     */

    default void postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 立即执行
     */
    default boolean postAtTime(Runnable r, long delayMillis) {
        return HANDLER.postAtTime(r, delayMillis);
    }

    /**
     * 移除消息队列中的消息
     */
    default void removeCallbacks() {
        HANDLER.removeCallbacksAndMessages(this);
    }
}
