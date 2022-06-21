package com.Gavin.service.impl;

import com.Gavin.entity.OrderDetail;
import com.Gavin.mapper.OrderDetailMapper;
import com.Gavin.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}