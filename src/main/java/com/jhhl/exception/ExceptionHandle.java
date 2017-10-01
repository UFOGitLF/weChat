package com.jhhl.exception;


import com.jhhl.base.model.BaseResponse;
import com.jhhl.consts.ServiceError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * Created by yangkang on 2017/4/1.
 */
@ControllerAdvice
public class ExceptionHandle{

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     * 统一处理异常
     *
     * @param  e  异常
     * @return json
     * @Author yangkang
     */
    @ExceptionHandler(value= Exception.class)
    @ResponseBody
    public BaseResponse handle (Exception e){
        if(e instanceof CheckResultException){
            CheckResultException exception =(CheckResultException) e;
            return BaseResponse.ResponseFail(e.getMessage(),exception.getCode());
        }else{

            logger.error("系统异常{}",e);
            return BaseResponse.ResponseFail(ServiceError.UNKNOWN_ERROR);
        }
    }

}
