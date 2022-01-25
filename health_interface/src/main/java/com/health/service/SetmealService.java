package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

   public void add(Setmeal setmeal, Integer[] checkgroupIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public   List<Setmeal> findAll();

   public Setmeal findById(int id);

   public List<Map<String, Object>> findSetmealCount();

    List<Integer> findCidbySid(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds, String tempImgId);

    void delete(Integer setMealId, String imgId);
}
