package com.itheima.reggie.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> insertCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功");
    }
    @GetMapping("/page")
    public R<Page> queryCategory(int page, int pageSize){
        //构造分页构造器
        Page pageInfo =new Page(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        if(categoryService.updateById(category))
        return R.success("修改分类信息成功！！！");
        return R.error("修改失败");
    }
    @DeleteMapping
    public R<String> deleteCategory(Long ids){
        //子表如果有外键约束引用主表主键，子表有记录不可删除，没记录可直接删除，默认 RESTRICT（默认行为）
        //可以通过CASCADE级联删除
        //如果没有外键约束，会立马删除主表，导致数据缺少完整性 根据实际情况进行业务逻辑处理，如果有记录，就不能直接删除。
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<Category>> queryCategoryList(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType()!=null, Category::getType, category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
