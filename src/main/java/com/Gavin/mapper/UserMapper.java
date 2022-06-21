package com.Gavin.mapper;

import com.Gavin.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Gavin
 * @description:
 * @className: UserMapper
 * @date: 2022/6/15 21:47
 * @version:0.1
 * @since: jdk14.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
