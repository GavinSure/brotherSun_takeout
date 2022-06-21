package com.Gavin.mapper;

import com.Gavin.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Gavin
 * @description:
 * @className: DishMapper
 * @date: 2022/6/4 19:45
 * @version:0.1
 * @since: jdk14.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
