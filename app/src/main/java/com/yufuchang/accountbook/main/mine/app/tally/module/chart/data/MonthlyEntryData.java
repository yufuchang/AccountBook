package com.yufuchang.accountbook.main.mine.app.tally.module.chart.data;



public class MonthlyEntryData {

    private Month month;
    private double expenseAmount;
    private double incomeAmount;

    public Month getMonth() {
        return month;
    }

    public MonthlyEntryData setMonth(Month month) {
        this.month = month;
        return this;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public MonthlyEntryData setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
        return this;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public MonthlyEntryData setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
        return this;
    }
}
