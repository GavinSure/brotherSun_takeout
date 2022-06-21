package com.Gavin.mapper;


import com.Gavin.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;



/**
 * @author: Gavin
 * @description:
 * @className: EmployeeMaper
 * @date: 2022/5/14 9:32
 * @version:0.1
 * @since: jdk14.0
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
