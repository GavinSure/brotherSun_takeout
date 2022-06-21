package com.Gavin.service;

import com.Gavin.dto.SetmealDto;
import com.Gavin.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author: Gavin
 * @description:
 * @className: SetmealService
 * @date: 2022/6/4 19:51
 * @version:0.1
 * @since: jdk14.0
 */
public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
