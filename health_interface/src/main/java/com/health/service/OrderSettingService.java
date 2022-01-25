package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

   public void add(List<OrderSetting> data);

    public List<Map> getOrderSettingByMonth(String date);

  public  void editNumberByDate(OrderSetting orderSetting);

   public PageResult pageQuery(QueryPageBean queryPageBean);

  public   void deleteByid(Integer id);
}
