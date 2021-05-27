package com.yufuchang.accountbook.main.mine.app.tally.module.setting;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.main.mine.app.tally.common.router.TallyRouter;
import com.yufuchang.accountbook.main.mine.ui.BaseActivity;
import com.yufuchang.accountbook.module.setting.SettingActivityBinding;

import static com.yufuchang.accountbook.base.utils.LogUtils.makeLogTag;



@Route(path = TallyRouter.SETTING)
public class SettingActivity extends BaseActivity {

    private static final String TAG = makeLogTag(SettingActivity.class);

    private SettingActivityBinding mBinding;
    private SettingViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);

        subscribeUi();
    }

    private void subscribeUi() {
        mBinding.setActivity(this);
        mBinding.setVm(mViewModel);
        mViewModel.getProcessMessage().observe(this, message -> {
            if (message == null) {
                dismissProcessDialog();
            } else {
                showProcessDialog(message);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setToolbarAsBack(view -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(self(), requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mViewModel.onRequestPermissionsResult(self(), requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
