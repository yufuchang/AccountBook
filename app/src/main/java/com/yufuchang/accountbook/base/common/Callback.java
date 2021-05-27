package com.yufuchang.accountbook.base.common;




public interface Callback<TData, TError> extends SimpleCallback<TData> {
    void success(TData data);

    void failure(TError error);
}
