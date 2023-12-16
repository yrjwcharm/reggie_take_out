package com.itheima.reggie.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return R.success("修改成功");
        return R.error("修改失败");
    }
    @DeleteMapping
    public R<String> deleteCategory(Long ids){
        if(categoryService.removeById(ids)) return R.success("删除成功");
        return R.error("删除失败");
    }
}
