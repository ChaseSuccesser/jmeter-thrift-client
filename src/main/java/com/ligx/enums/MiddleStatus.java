package com.ligx.enums;

/**
 * Author: ligongxing.
 * Date: 2017年12月15日.
 */
public enum MiddleStatus {

    SUCCESS(0),
    FAIL(1);

    private int code;

    MiddleStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
