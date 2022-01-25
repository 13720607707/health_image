package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Permission;
import com.health.pojo.Role;
import com.health.pojo.User;
import com.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.List;

/**
 * @author zs
 * @program: health_image
 * @description: 角色管理
 * @date 2022-01-13 16:58:47
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;
    //分页查询所有角色
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return roleService.findPage(queryPageBean);
    }

    @RequestMapping("/findAllPermission")
    public Result findAllPremission(){

        try {
            List<Permission> list= roleService.findAllPremission();
            return  new Result(true,"查询所有权限成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"查询所有权限失败");
        }
    }

    //添加角色
    @RequestMapping("/add")
    public Result add(Integer[] permissionIds, @RequestBody Role role) {
        try {

            if (role == null && role.getName() == null && role.getKeyword() == null) {
                return new Result(false, "角色为空");
            }
            //判断当前角色名或者关键字是否存在，如果存在，添加失败

            //添加角色
            roleService.add(permissionIds, role);
            return new Result(true, "添加角色成功");
        } catch (Exception e) {
            return new Result(false, "添加角色失败");
        }
    }
         @RequestMapping("/delete")
        public Result delete(Integer id){
             try {
                 roleService.delete(id);
                 return new Result(true,"删除角色成功");
             } catch (Exception e) {
                 e.printStackTrace();
                 return new Result(true,"删除角色失败");
             }
         }
         @RequestMapping("/findUsById")
        public  Result findUsById(Integer id){
             try {
                List<User> users= roleService.findUsById(id);
                if(users!=null||users.size()==0){
                    return new Result(false,"查询用户为空",users);
                }
                 return new Result(true,"查询用户成功",users);
             } catch (Exception e) {
                 e.printStackTrace();
                 return new Result(false,"查询用户失败");
             }
         }
         //根据角色拿到per用于编辑回显
         @RequestMapping("/findPermissionIdByrid")
         public  Result findPermissionIdByrid(Integer id){
             try {
                 List<Integer> permissions= roleService.findPermissionIdByrid(id);
                 return new Result(true,"查询权限成功",permissions);
             } catch (Exception e) {
                 e.printStackTrace();
                 return new Result(false,"查询权限失败");
             }


         }
//         编辑
    @RequestMapping("/edit")
    public  Result edit(Integer[] permissionIds,@RequestBody Role role){
        try {
            roleService.edit(permissionIds,role);
            return new Result(true,"编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑失败");
        }
    }





}
