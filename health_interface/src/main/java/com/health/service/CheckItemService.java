package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;

import java.util.List;

//服务接口
public interface CheckItemService {
    public void add(CheckItem checkItem);
    public PageResult pageQuery(QueryPageBean queryPageBean);

   public void deleteByid(Integer id);

  public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

   public List<CheckItem> findAll();
}
