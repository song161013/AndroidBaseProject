package com.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class MyAdapter<T> extends BaseAdapter<MyAdapter.ViewHold> {

    /**
     * 列表数据
     */
    private List<T> mDataSet;

    public MyAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void setData(List<T> data) {
        mDataSet = data;
        notifyDataSetChanged();
    }


    public abstract class ViewHold extends BaseAdapter.ViewHold {

        public ViewHold(int id) {
            super(id);
        }

        public ViewHold(@NonNull View itemView) {
            super(itemView);
        }
    }
}
