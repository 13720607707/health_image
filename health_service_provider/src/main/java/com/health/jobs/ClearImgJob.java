package com.health.jobs;

import com.health.constant.RedisConstant;
import com.health.utils.ImageStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

//实现定时清理垃圾图片
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //根据RedisPool保存的set集合进行差值计算，获得垃圾图片的名称集合
        Set<String> set=jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set!=null){
            for(String picName:set){
                //删除七牛云服务器上的图片
//                QiniuUtils.deleteFileFromQiniu(picName);
                ImageStoreUtils.delete(picName);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
                System.out.println("自定义任务执行，清理垃圾图片"+picName);

            } 
        }

    }
}
