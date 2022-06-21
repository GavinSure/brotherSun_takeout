package com.Gavin.dto;


import com.Gavin.entity.Setmeal;
import com.Gavin.entity.SetmealDish;
import lombok.Data;

import java.util.List;


@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
