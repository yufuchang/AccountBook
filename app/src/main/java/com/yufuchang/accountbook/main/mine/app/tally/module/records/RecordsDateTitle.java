package com.yufuchang.accountbook.main.mine.app.tally.module.records;


public class RecordsDateTitle {
    private int year;
    private int month;

    RecordsDateTitle(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}
