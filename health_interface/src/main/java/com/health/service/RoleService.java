package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;

import java.util.List;

public interface RoleService {
    public  PageResult findPage(QueryPageBean queryPageBean);

    List<Permission> findAllPremission();


    void add(Integer[] permissionIds, Role role);

    void delete(Integer id);

    List<User> findUsById(Integer id);

    List<Integer> findPermissionIdByrid(Integer id);

    void edit(Integer[] permissionIds, Role role);
}
