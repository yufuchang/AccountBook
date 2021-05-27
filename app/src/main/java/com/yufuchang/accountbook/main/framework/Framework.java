package com.yufuchang.accountbook.main.framework;


public class Framework {

    private static Framework mInstance;


    private Framework() {
    }

    private synchronized static Framework getInstance() {
        if (mInstance == null) {
            mInstance = new Framework();
        }
        return mInstance;
    }

    public static void onAppOnCreate() {
    }

}
