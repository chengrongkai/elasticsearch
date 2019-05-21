package com.crk.elasticsearch.domain;

import org.springframework.context.annotation.Bean;

import java.io.Serializable;


public class ResultBean implements Serializable{
    private boolean success;
    private String msg;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
