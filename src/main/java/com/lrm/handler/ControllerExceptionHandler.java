package com.lrm.handler;

import com.lrm.Exception.NoPermissionException;
import com.lrm.Exception.NotFoundException;
import com.lrm.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//全局异常处理 扫描所有的controller类
//为了不将抛出的异常直接暴露给用户
@RestControllerAdvice
public class ControllerExceptionHandler
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //负责处理权限不足异常
    @ExceptionHandler(NoPermissionException.class)
    public Result NoPermissionExceptionHandler(HttpServletRequest request, NoPermissionException noPermissionException) {
        logger.error("Request URL: {}, Exception : {}", request.getRequestURL(), noPermissionException);
        return Result.returnNoPermissionException(noPermissionException, request.getRequestURL());
    }

    //访问不存在的资源
    @ExceptionHandler(NotFoundException.class)
    public Result NotFoundExceptionHandler(HttpServletRequest request, NotFoundException notFoundException) {
        logger.error("Request URL: {}, Exception : {}", request.getRequestURL(), notFoundException);
        return Result.returnNotFoundException(notFoundException, request.getRequestURL());
    }
    //上传文件过大
    @ExceptionHandler(IOException.class)
    public Result IOEHandler(HttpServletRequest request, IOException ioException) {
        logger.error("Request URL: {}, Exception : {}", request.getRequestURL(), ioException);
        return Result.returnIOException(ioException, request.getRequestURL());
    }

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL: {}, Exception : {}", request.getRequestURL(),e);
        //排除我自定义的NotFound类 如果不加这个判断 会被error.html拦截 所以要判断一下ResponseStatus的"状态"
        if((AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null))
        {
            throw e;
        }
        return Result.returnNotDefinedError(request.getRequestURL());
    }

}
