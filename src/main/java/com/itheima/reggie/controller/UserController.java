package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
          String  code= ValidateCodeUtils.generateValidateCode4String(4).toString();
//          SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            log.info("验证码:{}",code);
//            session.setAttribute(phone,code);
            redisTemplate.opsForValue().set(phone,code);

            return R.success("手机验证码短信发送成功");
        }
        
        //生成随机的4位验证码
        //调用阿里云提供的短信服务API完成发送
        //需要将生成的验证码保存到Section
        return R.error("短信发送失败");
    }

    /**
     * 移动端登录
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,Object> map, HttpSession httpSession){
        log.info("map:{}",map);
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //进行验证码的比对(页面提交的验证码和Section中保存的验证码进行比对)
//        Object codeSection = httpSession.getAttribute(phone);
        Object codeSection = redisTemplate.opsForValue().get(phone);
        //如果比成功，说明登录成功
        if(codeSection!=null&& codeSection.equals(code)){
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(lambdaQueryWrapper);
            if(user==null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user =new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user",user.getId());

            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
