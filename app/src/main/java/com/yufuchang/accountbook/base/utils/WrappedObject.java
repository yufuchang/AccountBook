package com.yufuchang.accountbook.base.utils;


public class WrappedObject<T> {
    private T t;

    public WrappedObject(T object) {
        this.t = object;
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }
}
