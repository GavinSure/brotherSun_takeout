package com.Gavin.controller;

import com.Gavin.common.R;
import com.Gavin.entity.Employee;
import com.Gavin.mapper.EmployeeMapper;
import com.Gavin.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author: Gavin
 * @description:
 * @className: EmployeeController
 * @date: 2022/5/14 9:40
 * @version:0.1
 * @since: jdk14.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.密码加密
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //2.数据库查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);

        //3.如果没有查询到则返回登录失败结果
        if(emp==null){
            return R.error("没有该账号");
        }

        //4.密码比对
        if (!emp.getPassword().equals(password))
            return R.error("登陆失败");

        //5.看员工状态
        if(emp.getStatus()==0)
            return R.error("账号已禁用");

        //6.登陆成功，将员工id存入session
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

/*        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/

        Long empId=(long)request.getSession().getAttribute("employee");

/*        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    //员工信息分页查询
    @GetMapping("/page")        //name是搜索框内查询的数据
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping                             //数据回显在前端的双向数据绑定中处理
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        long id=Thread.currentThread().getId();
        log.info("线程id为：{}",id);
        Long empId=(Long)request.getSession().getAttribute("employee"); //注意强转的精度损失问题
        employee.setUpdateTime(LocalDateTime.now());     //状态值status在前端已经封装进employee里，无需在后端修改
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")            //restful风格传参
    public R<Employee> getById(@PathVariable long id){
        Employee employee=employeeService.getById(id);
        return R.success(employee);
    }

}
