package com.jhhl.consts;

/**
 *
 *
 * Created by yangkang on 17/9/28.
 */
public enum ServiceError {

    UNKNOWN_ERROR(-1,"系统错误"),

    RES_100(100,"提交参数错误"),

    RES_200(200,"请求成功"),
    ;
    private  int code;
    private  String message;

    ServiceError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
