package com.Gavin.service.impl;

import com.Gavin.dto.DishDto;
import com.Gavin.entity.Dish;
import com.Gavin.entity.DishFlavor;
import com.Gavin.entity.SetmealDish;
import com.Gavin.exception.CustomException;
import com.Gavin.mapper.DishMapper;
import com.Gavin.service.DishFlavorService;
import com.Gavin.service.DishService;
import com.Gavin.service.SetmealDishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Gavin
 * @description:
 * @className: DishServiceImpl
 * @date: 2022/6/4 19:46
 * @version:0.1
 * @since: jdk14.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

    /*
    * 新增菜品，同时保存对应的口味数据
    */
    @Transactional          //涉及多表要开启事务操作
    public void saveWithFlavor(DishDto dishDto){
        //保存菜品的基本信息到菜品表
        this.save(dishDto);


        Long dishId=dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors=dishDto.getFlavors();

        //将菜品id保存到口味表中
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }


    //根据id查询菜品信息和对应的口味信息
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品信息，从dish表查询
        Dish dish=this.getById(id);

        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor开始查询
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    //更新菜品信息，同时更新对应的口味信息
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        Long dishId=dishDto.getId();
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void removedishes(List<Long> ids) {
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Dish> dishQueryWrapper=new LambdaQueryWrapper<>();
        //套餐如果含有菜品则不能删除
        queryWrapper.in(SetmealDish::getDishId,ids);
        int count=setmealDishService.count(queryWrapper);
        if (count>0){
            throw new CustomException("套餐中包含该菜品，不能删除");
        }
        //套餐在售不能删除
        dishQueryWrapper.in(Dish::getId,ids);
        dishQueryWrapper.eq(Dish::getStatus,1);
        count=this.count(dishQueryWrapper);
        if (count>0)
            throw new CustomException("套餐在售，不能删除");

        //执行删除操作
        this.removeByIds(ids);
    }
}
