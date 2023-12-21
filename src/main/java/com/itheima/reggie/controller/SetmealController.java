package com.itheima.reggie.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithSetmealDish(setmealDto);
        return R.success("新增成功");
    }
    @GetMapping("/page")
    public R<Page<SetmealDto>> queryALlSetmeal(int page,int pageSize,String name){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件根据name 进行like模糊查询
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        setmealService.page(pageInfo,lambdaQueryWrapper);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<SetmealDto> list = pageInfo.getRecords().stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }
    @DeleteMapping
    public R<String> delSetmeal(@RequestParam List<Long> ids){
        setmealService.removeWidthDish(ids);
        return R.success("删除成功！！！");
    }
    @GetMapping("/list")
    public R<List<Setmeal>>  list(@RequestParam Map<String,Object> map){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(map.get("categoryId")!=null,Setmeal::getCategoryId,map.get("categoryId"));
        queryWrapper.eq(map.get("status")!=null,Setmeal::getStatus,map.get("status"));
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}


