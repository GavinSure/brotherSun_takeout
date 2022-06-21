package com.Gavin.service.impl;

import com.Gavin.entity.Employee;
import com.Gavin.mapper.EmployeeMapper;
import com.Gavin.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: Gavin
 * @description:
 * @className: EmployeeServiceImpl
 * @date: 2022/5/14 9:35
 * @version:0.1
 * @since: jdk14.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
