package com.Gavin.exception;

import com.Gavin.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author: Gavin
 * @description:全局异常处理类
 * @className: GlobalExceptionHandler
 * @date: 2022/5/15 20:55
 * @version:0.1
 * @since: jdk14.0
 */
@RestControllerAdvice
@ResponseBody           //通常用来返回json数据
@Slf4j
public class GlobalExceptionHandler {

    //针对新增员工账号冲突这种异常情况
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")){
            //从异常信息中分割出重复的账号名
            String[] split = ex.getMessage().split(" ");
            String msg=split[2]+"已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());



        return R.error(ex.getMessage());
    }
}
