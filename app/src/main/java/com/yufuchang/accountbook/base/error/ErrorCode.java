package com.yufuchang.accountbook.base.error;



public class ErrorCode {

    /**
     * 未知错误
     */
    public static final int UNKNOWN = -1;
    /**
     * SQLITE 数据库错误
     */
    public static final int SQL_ERR = 10001;
    /**
     * 内部错误
     */
    public static final int INTERNAL_ERR = 10002;
    /**
     * 不合法参数
     */
    public static final int ILLEGAL_ARGS = 10003;
}
