package com.base;


import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.action.ContextAction;

public abstract class BaseAdapter<VH extends BaseAdapter.ViewHold>
        extends RecyclerView.Adapter<VH> implements ContextAction {

    private Context mContext;
    private RecyclerView mRecyclerView;

    private OnItemClickListener mOnItemListener;

    private OnItemLongClickListener mOnItemLongListener;


    /**
     * RecyclerView 滚动监听
     */
    private OnScrollingListener mScrollingListener;
    /**
     * 自定义滚动监听
     */
    private ScrollListener mScrollListener;

    /**
     * 自条目点击事件
     */
    private SparseArray<OnChildClickListener> mOnChildListeners;

    /**
     * 子条目长按事件
     */
    private SparseArray<OnChildLongClickListener> mOnChildLongListeners;

    public BaseAdapter(Context context) {
        if (context == null) {
            return;
        }
        mContext = context;
    }

    public abstract class ViewHold extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private int mViewHoldPosition;

        public ViewHold(@LayoutRes int id) {
            this(LayoutInflater.from(mContext).inflate(id, getRecyclerView(), false));
        }

        public ViewHold(@NonNull View itemView) {
            super(itemView);

            if (mOnItemListener != null) {
                getItemVIew().setOnClickListener(this);
            }
            if (mOnItemLongListener != null) {
                getItemVIew().setOnLongClickListener(this);
            }
            if (mOnChildListeners != null) {
                for (int i = 0; i < mOnChildListeners.size(); i++) {
                    View childView = findViewById(mOnChildListeners.keyAt(i));
                    if (childView != null) {
                        childView.setOnClickListener(this);
                    }
                }
            }
            if (mOnChildLongListeners != null) {
                for (int i = 0; i < mOnChildLongListeners.size(); i++) {
                    View childView = findViewById(mOnChildLongListeners.keyAt(i));
                    childView.setOnLongClickListener(this);
                }
            }
        }

        public void markViewHoldPosition(int pos) {
            mViewHoldPosition = pos;
        }

        public int getViewHoldPosition() {
            return mViewHoldPosition;
        }

        public View getItemVIew() {
            return itemView;
        }

        public <V extends View> V findViewById(@IdRes int id) {
            return getItemVIew().findViewById(id);
        }


        @Override
        public void onClick(View v) {
            if (v == getItemVIew()) {
                if (mOnItemListener != null) {
                    mOnItemListener.onItemClick(getRecyclerView(), getItemVIew(), mViewHoldPosition);
                }
                return;
            }
            if (mOnChildListeners != null) {
                OnChildClickListener listener = mOnChildListeners.get(v.getId());
                if (listener != null) {
                    listener.onChildClick(getRecyclerView(), getItemVIew(), mViewHoldPosition);
                }
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if (v == getItemVIew()) {
                if (mOnItemLongListener != null) {
                    mOnItemLongListener.onItemLongClick(getRecyclerView(), getItemVIew(), mViewHoldPosition);
                }
            }
            if (mOnChildLongListeners != null) {
                OnChildLongClickListener listener = mOnChildLongListeners.get(v.getId());
                if (listener != null) {
                    listener.onChildLongClick(getRecyclerView(), getItemVIew(), mViewHoldPosition);
                }
            }
            return false;
        }

        public abstract void onBindView(int position);

    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        /**
         * 如果没有设置布局管理器，则使用默认的管理器
         */
        if (mRecyclerView.getLayoutManager() == null) {
            RecyclerView.LayoutManager layoutManager = generatorDefaultLayoutManager(mContext);
            if (layoutManager != null) {
                mRecyclerView.setLayoutManager(layoutManager);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mScrollingListener != null) {
            mRecyclerView.removeOnScrollListener(mScrollListener);
        }
        mScrollListener = null;
    }

    public RecyclerView.LayoutManager generatorDefaultLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    public void setScrollingListener(OnScrollingListener listener) {
        if (listener == null) {
            return;
        }
        mScrollingListener = listener;
        if (mScrollListener == null) {
            mScrollListener = new ScrollListener();
        } else {
            mRecyclerView.removeOnScrollListener(mScrollListener);
        }
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (mScrollingListener == null) return;
            /**
             * 滚动状态
             * SCROLL_STATE_IDLE:停止状态
             * SCROLL_STATE_DRAGGING : 正在滚动（用户在滑动下）
             * SCROLL_STATE_SETTLING :在没有外部控制下滚动到最终位置
             */
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                /**
                 * 检查能否滚动，正数：检查能否向下滚动
                 *             负数：检查能否向上滚动
                 */
                if (!recyclerView.canScrollVertically(1)) {
                    mScrollingListener.onScrollDown(recyclerView);
                }
                if (!recyclerView.canScrollVertically(-1)) {
                    mScrollingListener.onScrollingTop(recyclerView);
                }
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                mScrollingListener.onScrolling(recyclerView);
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    public void setItemOnClickListener(OnItemClickListener listener) {
        this.mOnItemListener = listener;
    }

    public void setItemLongListener(OnItemLongClickListener listener) {
        this.mOnItemLongListener = listener;
    }

    public void setChildListeners(@IdRes int id, OnChildClickListener listener) {
        if (mOnChildListeners == null) {
            mOnChildListeners = new SparseArray<>();
        }
        mOnChildListeners.put(id, listener);
    }

    public void setChildLongListener(@IdRes int id, OnChildLongClickListener listener) {
        if (mOnChildLongListeners == null) {
            mOnChildLongListeners = new SparseArray<>();
        }
        mOnChildLongListeners.put(id, listener);
    }

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        /**
         * @param rv       RecyclerView
         * @param itemView 被点击的item
         * @param position 被点击的条目位置
         */
        void onItemClick(RecyclerView rv, View itemView, int position);
    }

    /**
     * item 长按时间
     */
    public interface OnItemLongClickListener {
        /**
         * @param rv       RecyclerView
         * @param itemView 被点击的item
         * @param position 被点击的条目位置
         */
        void onItemLongClick(RecyclerView rv, View itemView, int position);
    }

    /**
     * item子view点击事件
     */
    public interface OnChildClickListener {
        /**
         * @param rv        RecyclerView
         * @param childView item的子view
         * @param position  被点击的条目位置
         */
        void onChildClick(RecyclerView rv, View childView, int position);
    }

    /**
     * item的子view长按事件
     */
    public interface OnChildLongClickListener {
        /**
         * @param rv        RecyclerView
         * @param childView item的子view
         * @param position  被点击的条目位置
         */
        void onChildLongClick(RecyclerView rv, View childView, int position);
    }

    /**
     * RecyclerView  滚动监听
     */
    public interface OnScrollingListener {
        /**
         * 滚动到顶部
         */
        void onScrollingTop(RecyclerView rv);

        /**
         * 滚动中
         */
        void onScrolling(RecyclerView rv);

        /**
         * 滚动到底部
         */
        void onScrollDown(RecyclerView rv);
    }
}
