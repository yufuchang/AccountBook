package com.yufuchang.accountbook.main.mine.app.tally.utils;


import com.yufuchang.accountbook.R;
import com.yufuchang.accountbook.base.utils.ResUtils;
import com.yufuchang.accountbook.main.mine.WorkerApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {

    /** 一日毫秒数 */
    public static final int DAY_MILLSECONDS = 24 * 60 * 60 * 1000;

    private static SimpleDateFormat mHourMinFormat = null;
    private static SimpleDateFormat mYearMonthDayFormat = null;
    private static SimpleDateFormat mMonthDayFormat = null;

    public synchronized static String getRecordDisplayDate(long timeMillis) {
        if (mHourMinFormat == null) {
            String yearFormat = ResUtils.getString(WorkerApp.getAppContext(), R.string.date_format_y_m_d);
            String monthFormat = ResUtils.getString(WorkerApp.getAppContext(), R.string.date_format_m_d);
            String todayFormat = ResUtils.getString(WorkerApp.getAppContext(), R.string.date_format_today_time);
            mYearMonthDayFormat = new SimpleDateFormat(yearFormat, Locale.getDefault());
            mMonthDayFormat = new SimpleDateFormat(monthFormat, Locale.getDefault());
            mHourMinFormat = new SimpleDateFormat(todayFormat, Locale.getDefault());
        }

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(timeMillis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentYear > year) {
            return mYearMonthDayFormat.format(calendar.getTime());
        }

        if (currentMonth > month || currentDay > day) {
            return mMonthDayFormat.format(calendar.getTime());
        }

        return mHourMinFormat.format(calendar.getTime());
    }

    /**
     * 计算该月份的天数
     *
     * @param year  年
     * @param month 月 1~12
     * @return 返回该月的天数
     */
    public static int getDaysTotalOfMonth(int year, int month) {
        if (month == 2) {
            // 闰年
            boolean leapYear = year % 4 == 0;
            if (leapYear) {
                return 29;
            } else {
                return 28;
            }
        }
        if (month == 1 || month == 3 || month == 5
                || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        }
        return 30;
    }
}
