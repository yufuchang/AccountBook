package com.yufuchang.accountbook.main.mine.app.tally.module.chart.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.PieChart;



public class MinePieChart extends PieChart {

    public MinePieChart(Context context) {
        super(context);
    }

    public MinePieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MinePieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new MinePieChartRenderer(this, mAnimator, mViewPortHandler);
    }
}
