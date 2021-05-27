package com.yufuchang.accountbook.base.common;


public class Result<TData, TError> extends ResultAbs<TData, TError> {
    public Result() {
    }

    public Result(TData data, TError error) {
        this.data = data;
        this.error = error;
    }
}
