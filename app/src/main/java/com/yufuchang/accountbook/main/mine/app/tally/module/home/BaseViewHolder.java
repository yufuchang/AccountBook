package com.yufuchang.accountbook.main.mine.app.tally.module.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yufuchang.accountbook.main.mine.app.tally.module.home.model.HomeDisplayData;




abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    BaseViewHolder(View view) {
        super(view);
    }

    /**
     * 绑定数据
     *
     * @param data 数据
     */
    abstract void bindData(HomeDisplayData data);
}
