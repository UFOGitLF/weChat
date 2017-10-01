package com.jhhl.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import com.jhhl.consts.MsgConst;
import com.jhhl.consts.NetConst;
import com.jhhl.consts.ServiceError;
import org.springframework.validation.BindingResult;

/**
 *
 * Created by yangkang on 17/1/28.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CheckResultException extends RuntimeException {

    private  int code;
    private String msg;

    public CheckResultException(BindingResult result) {
        super( result.getAllErrors().get(0).getDefaultMessage());
        this.code = MsgConst.FAIL;
        if (result.hasErrors()) {
            this.msg = result.getAllErrors().get(0).getDefaultMessage();
        }

    }

    public CheckResultException(String msg) {
        super(msg);
        code = NetConst.FAILED;
    }

    public CheckResultException(ServiceError error) {
        code = error.getCode();
        msg = error.getMessage();
    }

    public CheckResultException(ServiceError error, String message) {
        code = error.getCode();
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
