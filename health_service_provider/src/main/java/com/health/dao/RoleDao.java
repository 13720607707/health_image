package com.health.dao;

import com.github.pagehelper.Page;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
     public Page findPage(String queryString);

    public Set<Role> findByUserId(Integer id);

   public List<Permission> findAllPremission();

    void add(Role role);

    void setRoleAndPermissions(Map<String, Integer> map);

    void deleteAssocication(Integer id);

    void deleteRole(Integer id);

    List<User> findUsById(Integer id);

    List<Integer> findPermissionIdByrid(Integer id);

    void edit(Role role);
}
