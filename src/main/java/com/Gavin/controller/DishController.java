package com.Gavin.controller;

import com.Gavin.common.R;
import com.Gavin.dto.DishDto;
import com.Gavin.entity.Category;
import com.Gavin.entity.Dish;
import com.Gavin.entity.DishFlavor;
import com.Gavin.entity.Setmeal;
import com.Gavin.service.CategoryService;
import com.Gavin.service.DishFlavorService;
import com.Gavin.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Gavin
 * @description:
 * @className: DishController
 * @date: 2022/6/4 19:47
 * @version:0.1
 * @since: jdk14.0
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){    //json需要反序列化
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> pageInfo=new Page<>(page,pageSize);

        Page<DishDto> dishDtopage=new Page<>(page,pageSize);        //为了使菜品分类可以正常按名显示

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtopage,"records");
        List<Dish> records=pageInfo.getRecords();

        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long id=item.getCategoryId();
            String categoryname=categoryService.getById(id).getName();
            dishDto.setCategoryName(categoryname);

            return dishDto;
        }).collect(Collectors.toList());

        dishDtopage.setRecords(list);
        return R.success(dishDtopage);

    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){       //要查两张表
        DishDto dishDto=dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /*
    *  修改菜品
    */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }
/*
    @GetMapping("/list")
    public R<List<Dish>> list(Long categoryId){
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);
        List<Dish> dishes=dishService.list(queryWrapper);
        return R.success(dishes);
    }*/

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        //封装口味选项
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

    @PostMapping("/status/{st}")
    public R<String> status(@PathVariable int st, @RequestParam List<Long> ids){
        LambdaUpdateWrapper<Dish> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.in(Dish::getId,ids);
        updateWrapper.set(Dish::getStatus,st);
        dishService.update(updateWrapper);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.removedishes(ids);
        return R.success("删除成功");
    }

}
