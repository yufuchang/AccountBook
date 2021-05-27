package com.yufuchang.accountbook.main.mine.app.tally.module.chart.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.main.mine.app.tally.common.utils.TallyUtils;
import com.yufuchang.accountbook.main.mine.app.tally.databinding.CommonBindAdapter;
import com.yufuchang.accountbook.main.mine.app.tally.module.chart.data.DailyData;
import com.yufuchang.accountbook.main.mine.common.Font;



public class MarkerViewDailyData extends MarkViewMine {

    private TextView mTvData;
    private TextView mTvDate;
    private Entry mEntry;
    private OnClickListener mListener;

    public MarkerViewDailyData(Context context, int layoutResource) {
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
        DailyData dailyData = (DailyData) e.getData();
        String dayInfo = ResUtils.getString(getContext(), R.string.tally_day_info_format,
                dailyData.getYear(), dailyData.getMonth(), dailyData.getDayOfMonth());
        String moneyInfo = "¥" + TallyUtils.formatDisplayMoney(dailyData.getAmount());
        mTvDate.setText(dayInfo);
        mTvData.setText(moneyInfo);
        super.refreshContent(e, highlight);
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
