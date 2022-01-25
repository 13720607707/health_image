package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.CheckGroupDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;
import com.health.pojo.Setmeal;
import com.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组，同时关联检查项
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //操作t_checkgroup表
        checkGroupDao.add(checkGroup);

        //操作t_checkgroup_checkitems表
        Integer checkGroupId = checkGroup.getId();
        if (checkGroupId != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", checkGroupId);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);

            }
        }


    }

    //分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //编辑检查组，关联检查项
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组的基本信息，操作检查组t_checkgroup表
        checkGroupDao.edit(checkGroup);
        //先清理之前的关联关系，操作中间关系表t_checkgroup_checkitem表
        checkGroupDao.deleteAssocication(checkGroup.getId());
        //重新建立当前检查组与检查项的关联关系
        //操作t_checkgroup_checkitems表
        Integer checkGroupId = checkGroup.getId();
        if (checkGroupId != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", checkGroupId);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);

            }

        }
    }

    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
//删除检查组
    public void deleteByid(Integer id) {
//        long count = checkGroupDao.findCountByCheckgroupId(id);

//        if(count>0){
            //已关联检查项，清除关系
//            throw  new RuntimeException();
            checkGroupDao.deleteAssocication(id);
//        }
        checkGroupDao.deleteById(id);
    }

    @Override
    public long findSetmealByGroup(Integer id) {
        return checkGroupDao.findCountByCheckgroupId(id);
    }
}
