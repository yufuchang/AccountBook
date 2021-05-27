package com.yufuchang.accountbook.main.mine.app.tally.common.error;


import com.yufuchang.accountbook.base.common.IError;

import java.util.Locale;



public class ErrorUtils {

    public static String formatDisplayMsg(IError error) {
        return String.format(Locale.getDefault(), "[%d] %s", error.code(), error.msg());
    }
}
