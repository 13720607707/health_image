package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.OrderDao;
import com.health.dao.OrderSettingDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;
import com.health.pojo.OrderSetting;
import com.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

//预约设置服务
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

//批量导入数据
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private OrderDao orderDao;
    public void add(List<OrderSetting> list) {

        if(list!=null&&list.size()>0){
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已设置
                long countByOrderDate =orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate>0){
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //没有预约，插入操作
                    orderSettingDao.add(orderSetting);
                }

            }
        }

    }

    public List<Map> getOrderSettingByMonth(String date) {//2019-3
        String dateBegin = date + "-1";//2019-3-1
        String dateEnd = date + "-31";//2019-3-31
        System.out.println(dateBegin);
        Map map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number",orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        System.out.println(data);
        return data;
    }

    public void editNumberByDate(OrderSetting orderSetting) {
        //根据日期查询是否已经进行了预约设置
        Date orderDate = orderSetting.getOrderDate();
        long count= orderSettingDao.findCountByOrderDate(orderDate);
        if(count>0){
            //已经进行了预约设置，执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }


    }

    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage= queryPageBean.getCurrentPage();
        Integer pageSize =queryPageBean.getPageSize();
        String queryString =  queryPageBean.getQueryString();
        //基于mybatis分页助手
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = orderDao.findAll4Detail(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();


        return new PageResult(total,rows);
    }

    public void deleteByid(Integer id) {
        orderDao.deleteByid(id);
    }
}
