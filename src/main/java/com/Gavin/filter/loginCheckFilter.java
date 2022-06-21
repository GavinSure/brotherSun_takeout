package com.Gavin.filter;

import com.Gavin.common.BaseContext;
import com.Gavin.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Gavin
 * @description:  负责拦截登录权限的过滤器
 * @className: loginCheckFilter
 * @date: 2022/5/14 19:28
 * @version:0.1
 * @since: jdk14.0
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class loginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long id=Thread.currentThread().getId();
        log.info("线程id为：{}",id);
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI=request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);

        //定义不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //不需要处理  直接放行
        if (check){
            log.info("本次请求不需要处理");
            filterChain.doFilter(request,response);
            return;
        }
        //4-1判断登陆状态  如果已登录则直接放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录");
            //将id放入thread里
            Long empId=(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2判断移动端登陆状态  如果已登录则直接放行
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户已登录");
            //将id放入thread里
            Long empId=(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }


        log.info("用户未登录");
        //如果未登录则返回未登录结果，通过输出流方式向客户端返回响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /*
    * 检查本次是否需要放行
    * */
    public boolean check(String[] urls,String requestURI){
        for(String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match)
                return true;
        }
        return false;
    }
}
