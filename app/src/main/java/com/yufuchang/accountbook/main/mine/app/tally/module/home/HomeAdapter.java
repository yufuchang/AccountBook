package com.yufuchang.accountbook.main.mine.app.tally.module.home;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.main.mine.app.tally.module.home.model.HomeDisplayData;
import com.yufuchang.accountbook.main.mine.app.tally.module.records.RecordItemViewModel;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;

import java.util.ArrayList;
import java.util.List;


class HomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Activity mActivity;
    private HomeViewModel mViewModel;
    private HomeMonthInfoViewModel mMonthInfoViewModel;
    private RecordItemViewModel mRecordItemViewModel;
    private LayoutInflater mInflater;
    private List<HomeDisplayData> mDataList = new ArrayList<>();

    HomeAdapter(FragmentActivity activity, HomeViewModel viewModel, RecordItemViewModel recordItemViewModel) {
        mActivity = activity;
        mViewModel = viewModel;
        mMonthInfoViewModel = ViewModelProviders.of(activity).get(HomeMonthInfoViewModel.class);
        mRecordItemViewModel = recordItemViewModel;
        mInflater = LayoutInflater.from(mActivity);
    }

    void setDataList(List<HomeDisplayData> dataList) {
        if (dataList == null) {
            return;
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mDataList.size();
            }

            @Override
            public int getNewListSize() {
                return dataList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                HomeDisplayData oldData = mDataList.get(oldItemPosition);
                HomeDisplayData newData = dataList.get(newItemPosition);

                if (oldData.getType() != newData.getType()) {
                    return false;
                }

                switch (newData.getType()) {
                    case HomeDisplayData.TYPE_MONTH_INFO:
                    case HomeDisplayData.TYPE_RECENT_DAY_INFO:
                        return true;

                    case HomeDisplayData.TYPE_RECORD_ITEM:
                        Record oldRecord = (Record) oldData.getInternal();
                        Record newRecord = (Record) newData.getInternal();
                        return oldRecord.getId() == newRecord.getId();

                    default:
                        break;
                }
                return false;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }
        });
        mDataList.clear();
        mDataList.addAll(dataList);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {

            // 月消费、月收入信息模块
            case HomeDisplayData.TYPE_MONTH_INFO:
                return new ViewHolderMonthInfo(mActivity, mMonthInfoViewModel,
                        DataBindingUtil.inflate(mInflater, R.layout.tally_module_home_item_month_info, parent, false));

            // 今日账单数据
            case HomeDisplayData.TYPE_RECENT_DAY_INFO:
                return new ViewHolderTodayExpense(mActivity, mViewModel,
                        DataBindingUtil.inflate(mInflater, R.layout.tally_module_home_item_today_expense, parent, false));

            // 消费记录&支出记录 ITEM
            case HomeDisplayData.TYPE_RECORD_ITEM:
                return new ViewHolderRecordItem(mActivity, mRecordItemViewModel, DataBindingUtil.inflate(
                        mInflater, R.layout.tally_item_record_common, parent, false));

            // 底部 View
            case HomeDisplayData.TYPE_BOTTOM:
                return new ViewHolderBottom(mInflater.inflate(R.layout.tally_module_home_item_bottom, parent, false));

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindData(mDataList.get(position));
    }
}
