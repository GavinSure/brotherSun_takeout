package com.Gavin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * @author: Gavin
 * @description:
 * @className: MailUtil
 * @date: 2022/6/15 21:11
 * @version:0.1
 * @since: jdk14.0
 */
@Component
public class MailUtil {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    private static final String FROM="孙哥外卖<swimmingsure@163.com>";

    public void sendCodeEmail(String toEmail,String code){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(toEmail);
        message.setSubject("邮箱登录验证码");
        message.setText("您的验证码："+code+"  两分钟内有效");
        javaMailSender.send(message);
    }


}
