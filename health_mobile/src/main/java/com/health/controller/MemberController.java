package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.health.constant.MessageConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

//处理会员相关操作
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;


    //手机快速登录
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map){

        String telephone = (String) map.get("telephone");
        String validateCode =(String) map.get("validateCode");
        //从Redis中获取保存的验证码
        String CodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(CodeInRedis!=null&&validateCode!=null&&validateCode.equals(CodeInRedis)){
            //验证码正确
            //判断是否为会员
          Member member =   memberService.findByTelephone(telephone);
          if(member==null){
              //不是会员，自动完成注册
              member.setRegTime(new Date());
              member.setPhoneNumber(telephone);
              memberService.add(member);

          }
          //向浏览器写入Cookie，内容为手机号
            //向客户端浏览器写入cookie，内容为手机号
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);
            //将会员信息保存到Redis
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);


        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }






    }
}
