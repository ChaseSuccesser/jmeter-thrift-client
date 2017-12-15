package com.ligx.model;

/**
 * Author: ligongxing.
 * Date: 2017年12月15日.
 */
public class MiddleResult {

    private int code;

    private String msg;

    public MiddleResult(int code) {
        this.code = code;
    }

    public MiddleResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
