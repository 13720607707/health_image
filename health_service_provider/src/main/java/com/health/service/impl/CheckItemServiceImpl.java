package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.health.dao.CheckitemDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;
import com.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service(interfaceClass = CheckItemService.class,version = "1.0.0")
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckitemDao checkitemDao;

    public void add(CheckItem checkItem) {
        checkitemDao.add(checkItem);

    }


    public PageResult pageQuery(QueryPageBean queryPageBean) {
       Integer currentPage= queryPageBean.getCurrentPage();
       Integer pageSize =queryPageBean.getPageSize();
       String queryString =  queryPageBean.getQueryString();
        System.out.println(currentPage);
        System.out.println(pageSize);
       //基于mybatis分页助手
        PageHelper.startPage(currentPage, pageSize);
        List<CheckItem> page = checkitemDao.selectByCondition(queryString);
        PageInfo<CheckItem> PageInfo = new PageInfo<>(page);
        long total =PageInfo.getTotal();
        List<CheckItem> rows = PageInfo.getList();
        System.out.println(rows);
        return new PageResult(total,rows);
    }

    //根据删除检查项
    public void deleteByid(Integer id) {
        //判断检查项是否已经关联到检查组
        long count = checkitemDao.findCountByCheckItemId(id);
        if(count>0){
            //已关联检查组，不允许删除
            throw  new RuntimeException();
        }
        checkitemDao.deleteById(id);


    }


    public void edit(CheckItem checkItem) {
        checkitemDao.edit(checkItem);

    }


    public CheckItem findById(Integer id) {
        return  checkitemDao.findById(id);
    }


    public List<CheckItem> findAll() {
        return checkitemDao.findAll();
    }

}
