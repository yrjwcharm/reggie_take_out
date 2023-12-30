package com.itheima.reggie.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "套餐相关接口")
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    @ApiOperation("新增套餐接口")
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithSetmealDish(setmealDto);
        return R.success("新增成功");
    }
    @ApiOperation("套餐分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "name",value = "套餐名称",required = false)
    })
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
    @ApiOperation("套餐删除接口")
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delSetmeal(@RequestParam List<Long> ids){
        setmealService.removeWidthDish(ids);
        return R.success("删除成功！！！");
    }
    @ApiOperation("套餐条件查询接口")
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#map.get('categoryId')+'_'+#map.get('status')")
    public R<List<Setmeal>>  list(@RequestParam Map<String,Object> map){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(map.get("categoryId")!=null,Setmeal::getCategoryId,map.get("categoryId"));
        queryWrapper.eq(map.get("status")!=null,Setmeal::getStatus,map.get("status"));
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}


