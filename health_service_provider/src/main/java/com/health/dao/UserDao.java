package com.health.dao;


import com.github.pagehelper.Page;
import com.health.pojo.Role;
import com.health.pojo.Setmeal;
import com.health.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    public User findByUsername(String username);
    public Page findPage(String qureyString);
    public List<Role> findAllRoles();

    public void add(User user);

    public void setUserAndRoles(Map<String, Integer> map);


  public   List<Integer> findRoleIdByUid(Integer uid);

   public void edit(User user);

   public void deleteAssocication(Integer id);

   public   User findById(Integer id);

   public void deleteUser(Integer id);


}
