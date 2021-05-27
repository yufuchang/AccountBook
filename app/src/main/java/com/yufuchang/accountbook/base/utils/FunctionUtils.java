package com.yufuchang.accountbook.base.utils;


public class FunctionUtils {

    public static <T> void invokeSafe(T t, Task<T> task) {
        if (t != null) {
            task.run(t);
        }
    }

    public interface Task<T> {
        void run(T t);
    }
}
