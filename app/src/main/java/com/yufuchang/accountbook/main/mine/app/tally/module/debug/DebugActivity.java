package com.yufuchang.accountbook.main.mine.app.tally.module.debug;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.main.mine.ui.BaseActivity;



public class DebugActivity extends BaseActivity {

    private DebugActivityBinding mBinding;
    private DebugViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.tally_module_debug_activity);
        mViewModel = ViewModelProviders.of(this).get(DebugViewModel.class);

        subscribeUi();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setToolbarAsClose(v -> onBackPressed());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mViewModel.onRequestPermissionsResult(self(), requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void subscribeUi() {
        mBinding.setActivity(this);
        mBinding.setVm(mViewModel);
    }
}
