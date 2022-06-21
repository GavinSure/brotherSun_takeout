package com.Gavin.service.impl;

import com.Gavin.dto.SetmealDto;
import com.Gavin.entity.Setmeal;
import com.Gavin.entity.SetmealDish;
import com.Gavin.exception.CustomException;
import com.Gavin.mapper.SetmealMapper;
import com.Gavin.service.SetmealDishService;
import com.Gavin.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Gavin
 * @description:
 * @className: SetmealServiceImpl
 * @date: 2022/6/4 19:52
 * @version:0.1
 * @since: jdk14.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存dto的基本信息
        this.save(setmealDto);
        //保存套餐和菜品的关联信息
        List<SetmealDish> dishes=setmealDto.getSetmealDishes();
        dishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count=this.count(queryWrapper);
        //如果不能删除，抛出一个业务异常
        if(count>0)
            throw new CustomException("套餐正在售卖中，不能删除");
        //如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getName,ids);

        setmealDishService.remove(lambdaQueryWrapper);
    }


}
