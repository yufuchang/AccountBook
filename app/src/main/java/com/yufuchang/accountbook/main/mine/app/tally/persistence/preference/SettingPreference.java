package com.yufuchang.accountbook.main.mine.app.tally.persistence.preference;

import android.content.Context;
import android.content.SharedPreferences;



public class SettingPreference {

    private static final String FILE_NAME = "tally_setting_preference";

    private static final String KEY_HIDE_MONEY = "key_hide_money";
    private static final String KEY_BUDGET_MONTH = "key_budget_month";

    /** 是否隐藏金额 */
    public static boolean getHideMoney(Context context) {
        return getPreference(context).getBoolean(KEY_HIDE_MONEY, false);
    }

    /** 设置是否隐藏金额 */
    public static void setHideMoney(Context context, boolean hideMoney) {
        getPreference(context).edit().putBoolean(KEY_HIDE_MONEY, hideMoney).apply();
    }

    /** 月预算金额 */
    public static float getBudgetMonth(Context context) {
        return getPreference(context).getFloat(KEY_BUDGET_MONTH, 0);
    }

    /** 设置月预算金额 */
    public static void setBudgetMonth(Context context, float budget) {
        getPreference(context).edit().putFloat(KEY_BUDGET_MONTH, budget).apply();
    }

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }
}
