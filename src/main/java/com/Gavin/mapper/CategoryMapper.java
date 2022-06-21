package com.Gavin.mapper;


import com.Gavin.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Gavin
 * @description:
 * @className: CategoryMapper
 * @date: 2022/6/3 23:04
 * @version:0.1
 * @since: jdk14.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
