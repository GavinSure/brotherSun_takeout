package com.Gavin.service;

import com.Gavin.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: Gavin
 * @description:
 * @className: CategoryService
 * @date: 2022/6/3 23:07
 * @version:0.1
 * @since: jdk14.0
 */
public interface CategoryService extends IService<Category> {
     void remove(Long id);
}
