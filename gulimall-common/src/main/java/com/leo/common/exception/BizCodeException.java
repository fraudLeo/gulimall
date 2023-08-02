package com.leo.common.exception;

public enum BizCodeException {

    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常"),
    VAILD_EXCEPTION(10001,"参数校验格式失败");

    private int code;
    private String msg;

    BizCodeException(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
