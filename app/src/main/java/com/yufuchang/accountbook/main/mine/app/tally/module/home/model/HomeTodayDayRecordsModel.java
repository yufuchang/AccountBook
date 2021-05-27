package com.yufuchang.accountbook.main.mine.app.tally.module.home.model;


import com.yufuchang.accountbook.main.mine.app.tally.common.utils.TallyUtils;


public class HomeTodayDayRecordsModel {

    /** 近3日新增账单数量 */
    private int recent3DayRecordsCount;
    /** 今日消费金额 */
    private double toadyExpenseAmount;
    /** 今日收入金额 */
    private double todayIncomeAmount;

    public int getRecent3DayRecordsCount() {
        return recent3DayRecordsCount;
    }

    public void setRecent3DayRecordsCount(int recent3DayRecordsCount) {
        this.recent3DayRecordsCount = recent3DayRecordsCount;
    }

    public double getToadyExpenseAmount() {
        return toadyExpenseAmount;
    }

    public void setToadyExpenseAmount(double toadyExpenseAmount) {
        this.toadyExpenseAmount = toadyExpenseAmount;
    }

    public double getTodayIncomeAmount() {
        return todayIncomeAmount;
    }

    public void setTodayIncomeAmount(double todayIncomeAmount) {
        this.todayIncomeAmount = todayIncomeAmount;
    }

    public String getDisplayExpenseAmount() {
        return "¥" + TallyUtils.formatDisplayMoney(toadyExpenseAmount);
    }

    public String getDisplayIncomeAmount() {
        return "¥" + TallyUtils.formatDisplayMoney(todayIncomeAmount);
    }
}
