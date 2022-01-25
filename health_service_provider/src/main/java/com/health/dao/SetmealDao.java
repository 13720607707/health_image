package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckgroup(Map map);

    public Page<Setmeal> findByCondition(String queryString);

    public List<Setmeal> findAll();

    public Setmeal findById(int id);

   public List<Map<String, Object>> findSetmealCount();

    List<Integer> findCidBySid(Integer id);

    void editSetmeal(Setmeal setmeal);

    void deleteAssocication(Integer setmealId);

    void deleteSetmealById(Integer setMealId);
}
