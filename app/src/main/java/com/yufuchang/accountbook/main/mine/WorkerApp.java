package com.yufuchang.accountbook.main.mine;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.SparseArray;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yufuchang.accountbook.BuildConfig;
import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.UIUtils;
import com.yufuchang.accountbook.base.widget.LoadingLayout;
import com.yufuchang.accountbook.main.framework.Framework;

public class WorkerApp extends Application {

    private static Application mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        Global.init(this);
        Framework.onAppOnCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
        }
        if (!BuildConfig.DEBUG) {
//            TCAgent.init(this, BuildConfig.TALKING_DATA_APP_ID, BuildConfig.FLAVOR);
//            TCAgent.setReportUncaughtExceptions(true);
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        // 检查新版本
//        UpdateUtils.startNewClientVersionCheckBackground(this);
        // 初始化 LoadingLayout
        initLoadingLayout();
        // 初始化 ARouter
        ARouter.init(this);
    }

    public static Application getAppContext() {
        return mAppContext;
    }

    private void initLoadingLayout() {
        SparseArray<LoadingLayout.Config> globalConfig = LoadingLayout.getGlobalConfig();

        LoadingLayout.Config emptyConfig = new LoadingLayout.Config();
        emptyConfig.setIconRes(R.drawable.ic_loading_layout_empty);
        emptyConfig.setMessage(UIUtils.getString(this, R.string.loading_layout_message_empty));
        emptyConfig.setMessageTextStyle(R.style.TextAppearance_LoadingMessage);
        emptyConfig.setButtonPositiveBackgroundRes(R.drawable.bg_accent_btn_round);
        emptyConfig.setButtonPositiveText(UIUtils.getString(this, R.string.loading_layout_button_positive_text_empty));
        emptyConfig.setButtonPositiveTextStyle(R.style.TextAppearance_LoadingPositiveButton);

        LoadingLayout.Config errorConfig = new LoadingLayout.Config();
        errorConfig.setIconRes(R.drawable.ic_loading_layout_empty);
        errorConfig.setMessage(UIUtils.getString(this, R.string.loading_layout_message_error));
        errorConfig.setMessageTextStyle(R.style.TextAppearance_LoadingMessage);
        errorConfig.setButtonPositiveBackgroundRes(R.drawable.bg_accent_btn_round);
        errorConfig.setButtonPositiveText(UIUtils.getString(this, R.string.loading_layout_button_positive_text_error));
        errorConfig.setButtonPositiveTextStyle(R.style.TextAppearance_LoadingPositiveButton);

        globalConfig.append(LoadingLayout.STATUS_SUCCESS, new LoadingLayout.Config());
        globalConfig.append(LoadingLayout.STATUS_LOADING, new LoadingLayout.Config());
        globalConfig.append(LoadingLayout.STATUS_EMPTY, emptyConfig);
        globalConfig.append(LoadingLayout.STATUS_ERROR, errorConfig);
    }
}
