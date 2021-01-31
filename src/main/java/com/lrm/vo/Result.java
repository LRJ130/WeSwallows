package com.lrm.vo;

import com.lrm.Exception.NoPermissionException;
import com.lrm.Exception.NotFoundException;

//包装类
public class Result <T> {

    T t;

    Boolean isSuccess;

    String msg;

    StringBuffer url;


    //必须存在一个无参构造方法^^
    public Result()
    {

    }

    //正常类型的包装返回的结果
    public Result(T t, Boolean isSuccess, String msg) {
        this.t = t;
        this.isSuccess = isSuccess;
        this.msg = msg;
    }


    //自定义异常返回的结果
    //为什么不用泛型一次解决所有自定义异常？如果用泛型的话，参数就不能用异常类来封装，那么业务层只有throws出的对象有用，还需要在ControllerAdvice层定义msg和code，不如一次性抛出，感觉复杂程度差不多？
    //为什么static？因为调用的时候不需要初始化泛型 只有设成static方法才可以
    public  static Result returnNoPermissionException(NoPermissionException noPermissionException, StringBuffer url){
        Result result = new Result();
        result.t = null;
        result.isSuccess = false;
        result.msg = noPermissionException.getErrorMsg();
        result.url = url;
        return result;
    }

    public  static Result returnNotFoundException(NotFoundException notFoundException, StringBuffer url){
        Result result = new Result();
        result.t = null;
        result.isSuccess = false;
        result.msg = notFoundException.getErrorMsg();
        result.url = url;
        return result;
    }

    //其他未知异常返回的结果
    public static Result returnNotDefinedError(StringBuffer url){
        Result result = new Result();
        result.t = null;
        result.isSuccess = false;
        result.msg = "我也不知道发生甚么事了...";
        result.url = url;
        return result;
    }

}