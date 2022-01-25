package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.constant.RedisConstant;
import com.health.dao.SetmealDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Setmeal;
import com.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    //新增套餐信息，同时观察检查组
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId =setmeal.getId();
        this.setSetmealAndCheckgroup(setmealId,checkgroupIds);
        //将图片名称保存到Redis集合当中
        String fileName =setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);


    }

    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage =queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);

       Page<Setmeal> page=setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    public List<Setmeal> findAll() {
        List<Setmeal> list=setmealDao.findAll();
        return list;
    }
//套餐详情
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }
//查询套餐预约占比
    public List<Map<String, Object>> findSetmealCount() {
      List<Map<String,Object>>  list= setmealDao.findSetmealCount();
        return list ;
    }

    @Override
    public List<Integer> findCidbySid(Integer id) {
        return setmealDao.findCidBySid(id);
    }

    public void edit(Setmeal setmeal, Integer[] checkgroupIds, String tempImgId) {
      //把Redis中小集合中存的图片名称替换
        if (tempImgId != null && !tempImgId.equals("")) {
            //清理Redis中小集合内的原图片名
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, tempImgId);
            //将更新后的图片名称存入Redis
            String fileName = setmeal.getImg();
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
        }
        //编辑套餐
        setmealDao.editSetmeal(setmeal);
        //获取SetmealId
        Integer setmealId = setmeal.getId();
        setmealDao.deleteAssocication(setmealId);
        //编辑套餐和检查组关联关系
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //先清空关联关系
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map =new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.setSetmealAndCheckgroup(map);


            }


        }
    }
    //删除套餐

    public void delete(Integer setMealId, String imgId) {
        //删除Redis中小集合中的图片
        if (imgId != null && !imgId.equals("")) {
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, imgId);
        }
        //删除操作
        if (setMealId!=null&&!setMealId.equals("")){
            //先删除关联关系
            setmealDao.deleteAssocication(setMealId);
            //根据id删除套餐
            setmealDao.deleteSetmealById(setMealId);
        }


    }



    //设置套餐和检查组多对多关系，操作t_setmeal_checkgroup
    public void setSetmealAndCheckgroup(Integer setmealId,Integer[] checkgroupIds){
        if(checkgroupIds!=null&&checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map =new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.setSetmealAndCheckgroup(map);

                
            }
        }
    }
}
