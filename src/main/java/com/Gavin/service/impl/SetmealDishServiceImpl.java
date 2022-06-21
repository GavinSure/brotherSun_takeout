package com.Gavin.service.impl;

import com.Gavin.dto.DishDto;
import com.Gavin.dto.SetmealDto;
import com.Gavin.entity.SetmealDish;
import com.Gavin.mapper.SetmealDishMapper;
import com.Gavin.service.SetmealDishService;
import com.Gavin.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Gavin
 * @description:
 * @className: SetmealDishServiceImpl
 * @date: 2022/6/10 14:30
 * @version:0.1
 * @since: jdk14.0
 */
@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {


}
