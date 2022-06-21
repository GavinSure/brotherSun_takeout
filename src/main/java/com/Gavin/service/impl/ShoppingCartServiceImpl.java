package com.Gavin.service.impl;

import com.Gavin.entity.ShoppingCart;
import com.Gavin.mapper.ShoppingCartMapper;
import com.Gavin.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
