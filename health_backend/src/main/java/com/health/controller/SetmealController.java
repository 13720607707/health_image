package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.constant.RedisConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Setmeal;
import com.health.service.SetmealService;
import com.health.utils.ImageStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

//套餐管理
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;


    //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        String oriname = imgFile.getOriginalFilename();

        int index = oriname.lastIndexOf(".");
        String ext = oriname.substring(index - 1);

        //产生文件名
        String fileName = UUID.randomUUID().toString() + ext;
        try {
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);//redis集合中存入名称
//            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            ImageStoreUtils.upload(imgFile.getBytes(),fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            ;
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }


    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        return setmealService.pageQuery(queryPageBean);
    }
    @RequestMapping("/findCidbySid")
    public Result findCidbySid(Integer id){
        try {
            List<Integer> list= setmealService.findCidbySid(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    //根据套餐Id查询详情
    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Setmeal list = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }
    //修改
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")//权限校验
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds ,String tempImgId){
        try {
            setmealService.edit(setmeal,checkgroupIds,tempImgId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "编辑套餐失败");
        }
        return new Result(true,"编辑套餐成功");
    }
    //删除
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")//权限校验
    @RequestMapping("/delete")
    public Result delete(Integer setMealId,String imgId){
        try {
            setmealService.delete(setMealId,imgId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
        return new Result(true,"删除成功");
    }

}
