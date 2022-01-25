package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.CheckItem;
import com.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference(version = "1.0.0")
    private CheckItemService checkItemService;

    //x新增检查项
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")//权限校验
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(
                    checkItem
            );
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);

        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }


    }
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);

        return pageResult;


    }

    //删除检查项
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.deleteByid(
                    id
            );
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);

        } catch (RuntimeException e){

            return new Result(false,e.getMessage());
        }catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }

    }
    //编辑检查项
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")//权限校验
    @RequestMapping("/edit")
    public Result delete(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(
                    checkItem
            );
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);

        }catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }

    }
    //查
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
          CheckItem checkItem=  checkItemService.findById(
                    id
            );
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);

        }catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }
    //查所有

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
           List<CheckItem> list =checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);

        }catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }
}
