package com.lrm.validation;

import com.lrm.annotation.AccountInfoFormat;
import com.lrm.util.StringVerify;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * PasswordFormat注解 校验器 实现
 * @author 山水夜止 修改自网络
 * @date 2021-3-25
        */
public class AccountInfoValidator implements ConstraintValidator<AccountInfoFormat, Object> {

    /**
     * 可以包含字母
     * 结果为false或true true允许有字母 false不允许
     */
    Boolean letterPemmited;

    /**
     * 初始化方法， 在(懒加载)创建一个当前类实例后，会马上执行此方法
     *
     * 注: 此方法只会执行一次，即:创建实例后马上执行。
     */
    @Override
    public void initialize(AccountInfoFormat format) {
        letterPemmited = Boolean.parseBoolean(format.permit());
    }

    /**
     * 校验方法， 每个需要校验的请求都会走这个方法
     *
     * 注: 此方法可能会并发执行，需要根据实际情况看否是需要保证线程安全。
     *
     * @param value
     *         被校验的对象
     * @param context
     *         上下文
     *
     * @return 校验是否通过
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String msg = (String) value;
        //如果有汉字 就直接false 如果有字母且不允许 返回false
        boolean pass = StringVerify.isContainChinese(msg) || (msg.length() >12 || msg.length() <7) || (StringVerify.isContainLetter(msg) & !letterPemmited);
        return !pass;
    }

}

