package com.yufuchang.accountbook.base.common;


public abstract class ResultAbs<TData, TError> implements IResult<TData, TError> {
    protected TData data;
    protected TError error;

    public void setData(TData data) {
        this.data = data;
    }

    @Override
    public TData data() {
        return data;
    }

    public void setError(TError error) {
        this.error = error;
    }

    @Override
    public TError error() {
        return error;
    }

    @Override
    public boolean isOk() {
        return error() == null;
    }
}
