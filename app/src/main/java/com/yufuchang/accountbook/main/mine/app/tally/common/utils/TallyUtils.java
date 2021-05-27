package com.yufuchang.accountbook.main.mine.app.tally.common.utils;

import com.yufuchang.accountbook.main.mine.app.tally.utils.TimeUtils;

import java.text.DecimalFormat;



public class TallyUtils {

    private static final DecimalFormat DISPLAY_MONEY_FORMAT = new DecimalFormat("0.00");

    /** 格式化显示的金额 */
    public static String formatDisplayMoney(double money) {
        return DISPLAY_MONEY_FORMAT.format(money);
    }

    /** 格式化显示的时间 */
    public static String formatDisplayTime(long timeMills) {
        return TimeUtils.getRecordDisplayDate(timeMills);
    }
}
