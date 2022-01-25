package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.RoleDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;
import com.health.service.RoleService;
import com.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zs
 * @program: health_image
 * @description: roles具体实现类
 * @date 2022-01-13 17:04:44
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage =queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);

        Page<Role> page = roleDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Permission> findAllPremission() {
        return roleDao.findAllPremission();
    }

    @Override
    public void add(Integer[] permissionIds, Role role) {
        //操作t-role
        roleDao.add(role);
        //操作t_role_per表
        Integer roleId = role.getId();


        if(roleId!=null&&permissionIds.length>0){

            for (Integer perId : permissionIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("roleId",roleId);
                map.put("perId",perId);
                roleDao.setRoleAndPermissions(map);
            }

        }
    }

    @Override
    public void delete(Integer id) {
        //        清除关系表

        roleDao.deleteAssocication(id);
        roleDao.deleteRole(id);
    }

    @Override
    public List<User> findUsById(Integer id) {
        return roleDao.findUsById(id);
    }

    @Override
    public List<Integer> findPermissionIdByrid(Integer id) {
        return roleDao.findPermissionIdByrid(id);
    }

    @Override
    public void edit(Integer[] permissionIds, Role role) {
        roleDao.edit(role);

        Integer roleId = role.getId();
        roleDao.deleteAssocication(roleId);
        if(roleId!=null&& permissionIds.length>0){
            for (int i = 0; i <permissionIds.length ; i++) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("roleId",roleId);
                map.put("perId",permissionIds[i]);
                roleDao.setRoleAndPermissions(map);
            }
        }


    }
}
