package com.yufuchang.accountbook.base.common;



public interface IResult<TData, TError> {

    TData data();

    TError error();

    boolean isOk();
}
