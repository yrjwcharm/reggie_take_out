package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        //1.将页面中提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面中提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到就返回登录失败结果
        if (emp == null) return R.error("登录失败");
        //4. 密码比对，如果不匹配，也返回失败
        if (!emp.getPassword().equals(password)) return R.error("登录失败");
        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus()==0) return R.error("账号已禁用");
        //6.登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    //员工退出登录
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！！！");
    }
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.save(employee);
        return R.success("保存成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> query(int page, int pageSize, String name){
            log.info("page={},pageSize={},name={}",page,pageSize,name);
            //构造分页构造器
        Page pageInfo =new Page(page,pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        Long id = Thread.currentThread().getId();
        log.info("线程id为:{}",id);
        employeeService.updateById(employee);
        return R.success("员工信息更新成功");
    }
    //路径变量
    @GetMapping("/{id}")
    public R<Employee> queryEmpById(@PathVariable Long id){
        log.info("根据员工id查询员工信息");
//        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Employee::getId,id);
//        return R.success(employeeService.getOne(queryWrapper));
        Employee employee = employeeService.getById(id);
        if(employee !=null) return R.success(employee);
        return R.error("没有查询对应员工信息");
    }
}
