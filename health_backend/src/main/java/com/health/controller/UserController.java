package com.health.controller;

import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.CheckGroup;
import com.health.pojo.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.health.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//用户操作
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername() throws Exception {
        try {
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    @Reference
    private UserService userService;

    //    分页查询用户
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        return userService.findPage(queryPageBean);

    }

    @RequestMapping("/findAllPermission")
    public Map findAllPremission(HttpServletRequest request) {
        //获取当前登录用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HashMap map = new HashMap();

        for (GrantedAuthority permission : user.getAuthorities()) {
            map.put(permission.getAuthority(), "have");
        }
        return map;
    }

    @RequestMapping("/findAllRoles")
    public Result findAllRoles() {


        try {
            List<Role> list = userService.findAllRoles();
            return new Result(true, "查询角色失败", list);
        } catch (Exception e) {
            e.printStackTrace();
            ;
            return new Result(false, "查询角色失败");
        }


    }

    @RequestMapping("/add")
    public Result add(@RequestBody com.health.pojo.User user, Integer[] roleIds) {


        try {
//            密码加密
            System.out.println(user.getPassword());
            String encode = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encode);

            userService.add(user, roleIds);
            return new Result(true, "新增用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "新增用户失败");
        }


    }

    @RequestMapping("/findRoleIdByUid")
    public Result findRoleIdByUid(Integer id) {
        System.out.println(id);

        try {
            List<Integer> roleIdByUid = userService.findRoleIdByUid(id);

            return new Result(true, "查询角色id成功",roleIdByUid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询角色id失败");

        }


    }
    @RequestMapping("/edit")
    public Result edit(@RequestBody com.health.pojo.User user,Integer[] roleIds){
        try {


            String encode = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encode);
            userService.edit(roleIds,user);
            return new Result(true, "编辑用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "编辑用户成功");
        }
    }
    //根据Id查询检查组
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            com.health.pojo.User user=userService.findById(id);

            return new Result(true, "查询成功",user);
        }catch (Exception e){
            e.printStackTrace();;
            return new Result(false, "查询失败");
        }


    }

    ////删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            userService.delete(id);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
//    修改密码
    @RequestMapping("/changePassword")
    public Result changePassword(@RequestBody Map pd){
        //查询当前登录用户
        try {
            User loginUser =
                    (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            //根据用户名查找当前用户
            com.health.pojo.User user = userService.findByUsername(loginUser.getUsername());
            String password = (String) pd.get("password");
            String senPassword = (String) pd.get("senPassword");

            if (password != null && password.equals(senPassword)) {
                user.setPassword(new BCryptPasswordEncoder().encode(password));
            }
            userService.uploadPd(user);
            return new Result(true,"修改密码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改密码失败");
        }
    }
}
