package com.lrm.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;

//定义的未被授权的异常类
@Controller
//状态码
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoPermissionException extends RuntimeException {
    protected Integer errorCode;
    protected String errorMsg;

    public NoPermissionException() {

    }

    public NoPermissionException(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public NoPermissionException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
