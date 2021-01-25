package com.lrm.vo;

public class R <T> {

    T t;

    Boolean isSuccess;

    String msg;

    public R(T t, Boolean isSuccess, String msg) {
        this.t = t;
        this.isSuccess = isSuccess;
        this.msg = msg;
    }
}