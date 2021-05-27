package com.yufuchang.accountbook.base.utils;



public class WrappedLong {

    private long i;

    public WrappedLong(int i) {
        this.i = i;
    }

    public long get() {
        return i;
    }

    public void set(long i) {
        this.i = i;
    }
}
