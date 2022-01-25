package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.CheckGroup;
import com.health.pojo.Setmeal;
import com.health.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;
    //新增检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")//权限校验
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try{
        checkGroupService.add(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();;
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }


    }
    //根据Id查询检查组
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            CheckGroup checkGroup=checkGroupService.findById(id);

            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();;
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }


    }
    //分页查询
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")//权限校验
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return   checkGroupService.pageQuery(queryPageBean);



    }
    //根据Id查询检查组包含多个检查项Id
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){

        try{
            List<Integer> checkitemids=checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemids);
        }catch (Exception e){
            e.printStackTrace();;
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }






    }
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")//权限校验
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try{
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();;
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }


    }
    //查询所有检查组
    @RequestMapping("/findAll")
    public Result findAll(){
        try{
          List<CheckGroup>  list =  checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();;
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }



    }
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")//权限校验
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {


                long setmeals  =    checkGroupService.findSetmealByGroup(id);
                if(setmeals>0){
                    return new Result(false, "有关联的套餐，不能删除");
                }
            checkGroupService.deleteByid(
                    id
            );

            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);

        } catch (RuntimeException e){

            return new Result(false,e.getMessage());
        }catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }

    }
}
