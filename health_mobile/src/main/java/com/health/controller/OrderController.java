package com.health.controller;
//预约

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.pojo.Order;
import com.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    //在线体检预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");

        String s = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");

        //判断验证码是否正确
        if(s!=null&&validateCode!=null&&validateCode.equals(s)){
            //调用预约服务

            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型 微信or电话
            try {
                Result result =orderService.order(map);
                System.out.println(telephone+(String) map.get("orderDate"));
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }


        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
    //根据预约ID查询预约相关信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            System.out.println(id);
            Map map=orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }


    }


}
