package com.Gavin.common;

/**
 * @author: Gavin
 * @description: 基于ThreadLocal封装工具类 用户保存和获取当前登录用户id ThreadLocal是线程的局部变量
 * @className: BaseContext
 * @date: 2022/6/3 18:20
 * @version:0.1
 * @since: jdk14.0
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
