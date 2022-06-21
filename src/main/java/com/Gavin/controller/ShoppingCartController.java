package com.Gavin.controller;

import com.Gavin.common.BaseContext;
import com.Gavin.common.R;
import com.Gavin.entity.ShoppingCart;
import com.Gavin.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: Gavin
 * @description:
 * @className: ShoppingCartController
 * @date: 2022/6/21 17:50
 * @version:0.1
 * @since: jdk14.0
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /*
    * 添加购物车
    * */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId!=null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,dishId);
        }
        //查询当前菜品或者套餐是否在购物车中
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);


        //如果已经存在，就在原来数量基础上加一
        if (one!=null){
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);
        }else {
            //不存在则添加到购物车，数量默认为一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one=shoppingCart;
        }
        return R.success(one);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list=shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /*
    * 清空购物车
    * */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }
    /*
    * 删除一个数量
    * */

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        Long Id = shoppingCart.getDishId();
        if (Id!=null){
            queryWrapper.eq(ShoppingCart::getDishId,Id);


        }else {
            //如果删除的是套餐
            Id=shoppingCart.getSetmealId();
            queryWrapper.eq(ShoppingCart::getSetmealId,Id);
        }
        //查询数据库
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if (one.getNumber()==1){
            //从购物车中直接删除
            shoppingCartService.removeById(one);
        }
        else{
            Integer number = one.getNumber();
            one.setNumber(number-1);
            shoppingCartService.updateById(one);
        }
        return R.success("删除成功");
    }
}
