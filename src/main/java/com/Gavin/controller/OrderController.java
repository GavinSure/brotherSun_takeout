package com.Gavin.controller;

import com.Gavin.common.R;
import com.Gavin.entity.Orders;
import com.Gavin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<List<Orders>> userPage(int page,int pageSize){
        return null;
    }
}
