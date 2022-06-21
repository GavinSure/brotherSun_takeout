package com.Gavin.controller;

import com.Gavin.common.AsycThreadPoolManager;
import com.Gavin.common.R;
import com.Gavin.entity.User;
import com.Gavin.service.UserService;
import com.Gavin.util.MailUtil;
import com.Gavin.util.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author: Gavin
 * @description:
 * @className: UserController
 * @date: 2022/6/15 21:25
 * @version:0.1
 * @since: jdk14.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取邮箱
        String email=user.getPhone();

        if (StringUtils.isNotEmpty(email)){
            //生成随机的四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //发送短信
            AsycThreadPoolManager.getInstance().executeTask(()->mailUtil.sendCodeEmail(email,code));
            //保存验证码
            session.setAttribute(email,code);

            return R.success("邮箱验证码发送成功");
        }

        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        String email=map.get("phone").toString();

        String code=map.get("code").toString();
        Object codeInSession = session.getAttribute(email);

        if (codeInSession!=null&&codeInSession.equals(code)){
            //比对成功说明登陆成功
            //判断当前手机号是否为新用户
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,email);

            User user = userService.getOne(queryWrapper);
            if (user==null){
                user=new User();
                user.setPhone(email);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}
