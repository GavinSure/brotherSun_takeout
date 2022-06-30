package com.Gavin.controller;

import com.Gavin.common.R;
import com.Gavin.dto.DishDto;
import com.Gavin.dto.OrdersDto;
import com.Gavin.entity.OrderDetail;
import com.Gavin.entity.Orders;
import com.Gavin.service.OrderDetailService;
import com.Gavin.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Gavin
 * @description:
 * @className: OrderController
 * @date: 2022/6/21 20:27
 * @version:0.1
 * @since: jdk14.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService detailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.orderByDesc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        //封装dto
        BeanUtils.copyProperties(pageInfo, ordersDtoPage,"records");
        List<Orders> records=pageInfo.getRecords();

        List<OrdersDto> list=records.stream().map((item)->{
            OrdersDto ordersDto=new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            Long id = item.getId();
            LambdaQueryWrapper<OrderDetail> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId,id);
            List<OrderDetail> list1 = detailService.list(wrapper);
            ordersDto.setOrderDetails(list1);
            return  ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(list);
        return R.success(ordersDtoPage);
    }
}
