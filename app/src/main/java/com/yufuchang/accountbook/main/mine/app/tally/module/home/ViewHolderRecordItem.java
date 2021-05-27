package com.yufuchang.accountbook.main.mine.app.tally.module.home;

import android.app.Activity;

import com.yufuchang.accountbook.main.mine.app.tally.module.home.model.HomeDisplayData;
import com.yufuchang.accountbook.main.mine.app.tally.module.records.RecordItemViewModel;
import com.yufuchang.accountbook.main.mine.app.tally.persistence.model.Record;
import com.yufuchang.accountbook.tally.module.records.RecordItemBinding;



public class ViewHolderRecordItem extends BaseViewHolder {

    private Activity mActivity;
    private RecordItemBinding mBinding;
    private RecordItemViewModel mViewModel;

    ViewHolderRecordItem(Activity activity, RecordItemViewModel viewModel, RecordItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mActivity = activity;
        mViewModel = viewModel;
    }

    void bindData(HomeDisplayData data) {
        if (data != null && data.getInternal() != null && data.getInternal() instanceof Record) {
            Record record = (Record) data.getInternal();
            mBinding.setActivity(mActivity);
            mBinding.setData(record);
            mBinding.setVm(mViewModel);
            mBinding.executePendingBindings();
        }
    }
}
