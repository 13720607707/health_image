package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Member;
import com.health.service.MemberService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员管理
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;

    //x新增检查项
    @RequestMapping("/add")
    public Result add(@RequestBody Member member) {
        try {
            memberService.add(
                    member
            );
            return new Result(true, MessageConstant.ADD_MEMBER_SUCCESS);

        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MEMBER_FAIL);
        }


    }
//
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = memberService.pageQuery(queryPageBean);

        return pageResult;

    }



//
//    //删除检查项
//
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            memberService.deleteByid(
                    id
            );
            return new Result(true, MessageConstant.DELETE_MEMBER_SUCCESS);

        } catch (RuntimeException e){

            return new Result(false,e.getMessage());
        }catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MEMBER_FAIL);
        }

    }
    //编辑检查项
    @RequestMapping("/edit")
    public Result delete(@RequestBody Member member) {
        try {
            memberService.edit(
                    member
            );
            return new Result(true, MessageConstant.EDIT_MEMBER_SUCCESS);

        }catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MEMBER_FAIL);
        }

    }
    //查
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
          Member checkItem=  memberService.findById(
                    id
            );
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);

        }catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }
    //查所有
//    @RequestMapping("/findAll")
//    public Result findAll() {
//        try {
//           List<CheckItem> list =memberService.findAll();
//            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
//
//        }catch (Exception e) {
//
//            e.printStackTrace();
//            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
//        }
//
//    }
}
