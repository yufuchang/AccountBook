package com.yufuchang.accountbook.base.common;


public class NonThrowError implements IError {

    private int code;
    private String msg;

    public NonThrowError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String msg() {
        return this.msg;
    }
}
