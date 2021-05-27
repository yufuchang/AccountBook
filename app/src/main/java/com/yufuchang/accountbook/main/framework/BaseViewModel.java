package com.yufuchang.accountbook.main.framework;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.main.concurrency.MineExecutors;


public class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(Application application) {
        super(application);
    }

    protected void showToastShort(@StringRes int messageResId) {
        showToastShort(ResUtils.getString(getApplication(), messageResId));
    }

    protected void showToastShort(String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MineExecutors.executeOnUiThread(() ->
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showToastLong(@StringRes int messageResId) {
        showToastLong(ResUtils.getString(getApplication(), messageResId));
    }

    protected void showToastLong(String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MineExecutors.executeOnUiThread(() ->
                    Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show());
        } else {
            Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
        }
    }

    protected void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MineExecutors.executeOnUiThread(runnable);
        } else {
            runnable.run();
        }
    }
}
