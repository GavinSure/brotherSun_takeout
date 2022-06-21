package com.Gavin.service;

import com.Gavin.dto.DishDto;
import com.Gavin.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Gavin
 * @description:
 * @className: DishService
 * @date: 2022/6/4 19:46
 * @version:0.1
 * @since: jdk14.0
 */
public interface DishService extends IService<Dish> {
     //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish dish_flavor
     void saveWithFlavor(DishDto dishDto);
     //根据id查询菜品信息和对应的口味信息
     DishDto getByIdWithFlavor(Long id);
     //更新菜品信息，同时更新对应的口味信息
     void updateWithFlavor(DishDto dishDto);
     //批量删除
     void removedishes(List<Long> ids);
}
