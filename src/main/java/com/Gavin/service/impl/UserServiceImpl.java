package com.Gavin.service.impl;

import com.Gavin.entity.User;
import com.Gavin.mapper.UserMapper;
import com.Gavin.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: Gavin
 * @description:
 * @className: UserServiceImpl
 * @date: 2022/6/15 21:47
 * @version:0.1
 * @since: jdk14.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
