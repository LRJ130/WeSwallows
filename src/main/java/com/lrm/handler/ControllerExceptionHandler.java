package com.lrm.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//扫描所有的controller类
@ControllerAdvice
public class ControllerExceptionHandler
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //负责处理所有的异常
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL: {}, Exception : {}", request.getRequestURL(),e);
        //排除我自定义的NotFound类 如果不加这个判断 会被error.html拦截 所以要判断一下ResponseStatus的"状态"
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
        {
            throw e;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }

}
