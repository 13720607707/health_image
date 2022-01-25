package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    public void add(CheckGroup checkGroup);
    public void setCheckGroupAndCheckItem(Map map);

   public Page<CheckGroup>  findByCondition(String queryString);

    public CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup);

   public void deleteAssocication(Integer id);

    public List<CheckGroup> findAll();

    public long findCountByCheckgroupId(Integer id);

    public void deleteById(Integer id);

   public long findCountByCheckgroupIdinMeal(Integer id);
}
