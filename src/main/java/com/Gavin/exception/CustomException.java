package com.Gavin.exception;

/**
 * @author: Gavin
 * @description: 自定义业务异常类
 * @className: CustomException
 * @date: 2022/6/4 20:15
 * @version:0.1
 * @since: jdk14.0
 */
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }
}
