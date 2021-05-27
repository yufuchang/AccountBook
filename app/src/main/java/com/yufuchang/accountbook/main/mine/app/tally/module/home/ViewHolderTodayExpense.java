package com.yufuchang.accountbook.main.mine.app.tally.module.home;

import android.app.Activity;

import com.yufuchang.accountbook.main.mine.app.tally.module.home.model.HomeDisplayData;
import com.yufuchang.accountbook.main.mine.app.tally.module.home.model.HomeTodayDayRecordsModel;
import com.yufuchang.accountbook.tally.module.home.TodayExpenseItemBinding;


class ViewHolderTodayExpense extends BaseViewHolder {

    private Activity mActivity;
    private HomeViewModel mViewModel;
    private TodayExpenseItemBinding mBinding;

    ViewHolderTodayExpense(Activity activity, HomeViewModel viewModel, TodayExpenseItemBinding binding) {
        super(binding.getRoot());
        mActivity = activity;
        mViewModel = viewModel;
        mBinding = binding;
    }

    @Override
    void bindData(HomeDisplayData data) {
        if (data != null && data.getInternal() != null && data.getInternal() instanceof HomeTodayDayRecordsModel) {
            HomeTodayDayRecordsModel todayModel = (HomeTodayDayRecordsModel) data.getInternal();
            mBinding.setActivity(mActivity);
            mBinding.setVm(mViewModel);
            mBinding.setData(todayModel);
            mBinding.executePendingBindings();
        }
    }
}
