package com.yufuchang.accountbook.main.mine.app.tally.common.server;


import android.support.annotation.Keep;

import com.alibaba.fastjson.annotation.JSONField;



@Keep
public class BaseResponse {
    @JSONField(name = "status")
    private int status;
    @JSONField(name = "message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
