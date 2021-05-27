package com.yufuchang.accountbook.main.mine.app.tally.module.chart.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.main.mine.app.tally.common.utils.TallyUtils;
import com.yufuchang.accountbook.main.mine.app.tally.databinding.CommonBindAdapter;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.Month;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.MonthlyEntryData;
import com.yufuchang.accountbook.main.mine.common.Font;


public class MarkerViewMonthData extends MarkViewMine {

    private TextView mTvData;
    private TextView mTvDate;
    private Entry mEntry;
    private OnClickListener mListener;

    public MarkerViewMonthData(Context context, int layoutResource) {
        super(context, layoutResource);
        mTvData = findViewById(R.id.tvData);
        mTvDate = findViewById(R.id.tvDate);
        CommonBindAdapter.setTypeFace(mTvData, Font.QUICKSAND_MEDIUM);
        CommonBindAdapter.setTypeFace(mTvDate, Font.QUICKSAND_BOLD);

        setOnClickListener(v -> {
            if (mListener != null && mEntry != null) {
                mListener.onClick(v, mEntry);
            }
        });
    }

    public void setOnClickListener(OnClickListener l) {
        mListener = l;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mEntry = e;
        MonthlyEntryData entryData = (MonthlyEntryData) e.getData();
        Month month = entryData.getMonth();
        String monthInfo = ResUtils.getString(getContext(), R.string.tally_month_info_format,
                month.getYear(), month.getMonth());
        String moneyInfo = ResUtils.getString(getContext(), R.string.tally_income_and_expense_format,
                "¥" + TallyUtils.formatDisplayMoney(entryData.getIncomeAmount()),
                "¥" + TallyUtils.formatDisplayMoney(entryData.getExpenseAmount()));
        mTvDate.setText(monthInfo);
        mTvData.setText(moneyInfo);
        super.refreshContent(e, highlight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param view  view
         * @param entry 数据
         */
        void onClick(View view, Entry entry);
    }
}
