package com.yufuchang.accountbook.main.mine;

import android.content.Context;



public class Global {

    private volatile static Global mInstance = null;

    private Context mAppContext = null;

    private Global() {
    }

    public static Global getInstance() {
        if (mInstance == null) {
            synchronized (Global.class) {
                if (mInstance == null) {
                    mInstance = new Global();
                }
            }
        }
        return mInstance;
    }

    public static void init(Context context) {
        getInstance().setAppContext(context);
    }

    private void setAppContext(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public Context getmAppContext() {
        return mAppContext;
    }
}
