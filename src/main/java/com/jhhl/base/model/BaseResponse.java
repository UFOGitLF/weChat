package com.jhhl.base.model;


import com.jhhl.consts.MsgConst;
import com.jhhl.consts.ServiceError;

/**
 *
 * Created by 杨康 on 2016/7/20.
 */
public class BaseResponse {

    private String msg;

    private int status;

    private Object data;

    public BaseResponse(String msg, int status, Object data) {
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    public static BaseResponse ResponseSucc(String msg, Object data) {
        return new BaseResponse(msg, MsgConst.SUCCESS, data);
    }

    public static BaseResponse ResponseSucc(String msg) {
        return ResponseSucc(msg,null);
    }

    public static BaseResponse ResponseFail(String msg,int status) {
        return new BaseResponse(msg, status, null);
    }

    public static BaseResponse ResponseFail(String msg) {
        return ResponseFail(msg,MsgConst.FAIL);
    }

    public static BaseResponse ResponseFail(ServiceError serviceError) {
        return ResponseFail(serviceError.getMessage(),serviceError.getCode());
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
