package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.CheckItem;

import java.util.List;

public interface CheckitemDao {
    public void add(CheckItem checkItem) ;
    public Page<CheckItem>  selectByCondition(String quareString);
    public long findCountByCheckItemId(Integer id);
    public void deleteById(Integer id);
    public void edit(CheckItem checkItem) ;
    public CheckItem findById(Integer id);

    public  List<CheckItem> findAll();
}
