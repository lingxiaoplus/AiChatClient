package com.lingxiao.chat.common.widget.recycler;

/**
 * Created by lingxiao on 2018/3/17.
 */

public interface AdapterCallback<Data> {
    /**
     * @param data 数据
     * @param holder
     */
    void updateData(Data data,RecyclerAdapter.ViewHolder<Data> holder);
}
