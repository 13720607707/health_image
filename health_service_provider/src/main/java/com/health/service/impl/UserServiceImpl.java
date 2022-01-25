package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.PermissionDao;
import com.health.dao.RoleDao;
import com.health.dao.UserDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;
import com.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    //根据用户名查询数据库获取用户信息和角色信息，同时查询查询角色关联的权限信息
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//基本信息，不包含角色
        if(user==null){
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roles= roleDao.findByUserId(userId);
        //根据角色查权限
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色id查权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);//角色关联权限


        }
        user.setRoles(roles);



        return user;
    }

    public PageResult findPage(QueryPageBean queryPageBean){
        Integer currentPage =queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);

        Page<User> page = userDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Role> findAllRoles() {
        return userDao.findAllRoles();
    }

    @Override
    public void add(User user, Integer[] roleIds) {
       //操作t-user
        userDao.add(user);
        //操作t_user_roles表
        Integer userId = user.getId();


        if(userId!=null&&roleIds.length>0){

            for (Integer roleId : roleIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("userId",userId);
                map.put("roleId",roleId);
                userDao.setUserAndRoles(map);
            }

        }
    }

    @Override
    public List findRoleIdByUid(Integer uid) {

        System.out.println(userDao.findRoleIdByUid(uid));
         return userDao.findRoleIdByUid(uid);

    }

    @Override
    public void edit(Integer[] roleIds, User user) {
        //修改检查组的基本信息，操作检查组t_checkgroup表
//        System.out.println(user);
//        System.out.println(roleIds);
        userDao.edit(user);
        //先清理之前的关联关系，操作中间关系表t_checkgroup_checkitem表
        userDao.deleteAssocication(user.getId());
        //重新建立当前检查组与检查项的关联关系
        //操作t_user_role表
        Integer userId = user.getId();
        if (userId != null && roleIds.length > 0) {
            for (Integer roleId : roleIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("userId",userId);
                map.put("roleId",roleId);
                userDao.setUserAndRoles(map);
            }

        }
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public void delete(Integer id) {
//        清除关系表
        System.out.println(id);
        userDao.deleteAssocication(id);
        userDao.deleteUser(id);


    }
//更新密码
    @Override
    public void uploadPd(User user) {
        userDao.edit(user);
    }

}



