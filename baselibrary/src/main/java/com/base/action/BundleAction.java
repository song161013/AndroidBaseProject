package com.base.action;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;


public interface BundleAction {

    @Nullable
    Bundle getBundle();

    default int getInt(String name) {
        return getInt(name, 0);
    }

    default int getInt(String name, int defaultValue) {
        return getBundle() == null ? defaultValue : getBundle().getInt(name, defaultValue);
    }

    default Long getLong(String name) {
        return getLong(name, 0);
    }

    default long getLong(String name, int defaultValue) {
        return getBundle() == null ? defaultValue : getBundle().getLong(name, defaultValue);
    }

    default float getFloat(String name) {
        return getFloat(name, 0);
    }

    default float getFloat(String name, int defaultVale) {
        return getBundle() == null ? defaultVale : getBundle().getFloat(name, defaultVale);
    }

    default double getDouble(String name) {
        return getDouble(name, 0);
    }

    default double getDouble(String name, int defauleValue) {
        return getBundle() == null ? defauleValue : getBundle().getDouble(name);
    }

    default String getString(String name) {
        return getBundle() == null ? null : getBundle().getString(name);
    }

    default <P extends Parcelable> P getParcelable(String name) {
        return getBundle() == null ? null : getBundle().getParcelable(name);
    }

    @SuppressWarnings("unchecked")
    default <S extends Serializable> S getSerializable(String name) {
        return getBundle() == null ? null : (S) getBundle().getSerializable(name);
    }

    default ArrayList<String> getStringArrayList(String name) {
        return getBundle() == null ? null : getBundle().getStringArrayList(name);
    }

    default ArrayList<Integer> getIntegerArrayList(String name) {
        return getBundle() == null ? null : getBundle().getIntegerArrayList(name);
    }


}
