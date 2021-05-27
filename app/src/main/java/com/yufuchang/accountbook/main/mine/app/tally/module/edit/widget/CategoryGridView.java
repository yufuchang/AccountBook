package com.yufuchang.accountbook.main.mine.app.tally.module.edit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;



public class CategoryGridView extends GridView {

    public CategoryGridView(Context context) {
        super(context);
    }

    public CategoryGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(21)
    public CategoryGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, defStyleAttr);
    }

    @TargetApi(21)
    public CategoryGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
