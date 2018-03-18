package com.lingxiao.chat.common.widget.recycler;

import android.annotation.SuppressLint;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingxiao.chat.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lingxiao on 2018/3/17.
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> mDataList;

    private AdapterListener<Data> mListener;

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mListener = listener;
        this.mDataList = dataList;
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter() {
        this(null);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType, parent, false);
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        //设置view的tag为viewholder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder);
        //界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        //绑定callback
        holder.callback = this;
        return holder;
    }

    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 表明该参数、变量或者函数返回值应该是一个 layout布局文件类型的资源
     *
     * @return xml文件的id，用于创建viewholder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        //触发holder的绑定
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一条数据，通知更新
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemChanged(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据，通知这个集合更新
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据，通知这个集合更新
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除数据
     */
    public void clear(Data data) {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合其中包括了清空
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null && dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (mListener != null) {
            //得到当前对应适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (mListener != null) {
            //得到当前对应适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            mListener.onItemLongClick(viewHolder, mDataList.get(pos));
        }
        return true;
    }

    public void setListener(AdapterListener<Data> adapterListener) {
        mListener = adapterListener;
    }

    public interface AdapterListener<Data> {

        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        private AdapterCallback callback;
        protected Data mData;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }


        protected abstract void onBind(Data data);

        /**
         * holder自己对对应的参数进行更新
         */
        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.updateData(data, this);
            }
        }
    }
}
