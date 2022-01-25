package com.health.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Role;
import com.health.pojo.Setmeal;
import com.health.pojo.User;

import java.util.List;

public interface UserService {
    public User findByUsername(String username);
    public PageResult findPage(QueryPageBean queryPageBean);
    public List<Role> findAllRoles();

    public void add(User user, Integer[] roleIds);



   public List<Integer> findRoleIdByUid(Integer uid);

  public   void edit(Integer[] roleIds, User user);

   public User findById(Integer id);

   public void delete(Integer id);

    public void uploadPd(User user);
}
