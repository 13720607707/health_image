package com.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.health.dao.MemberDao;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Member;
import com.health.service.MemberService;
import com.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//会员服务
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }
//保存会员信息
    public void add(Member member) {
        String password = member.getPassword();
        if(password!=null){
            //使用MD5将明文密码加密
            password = MD5Utils.md5(password);
            member.setPassword(password);


        }
        memberDao.add(member);

    }

    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCount =new ArrayList<>();
        for (String month : months) {
            String date = month + ".31";
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);

        }
        return memberCount;
    }
//分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage= queryPageBean.getCurrentPage();
        Integer pageSize =queryPageBean.getPageSize();
        String queryString =  queryPageBean.getQueryString();
        //基于mybatis分页助手
        PageHelper.startPage(currentPage,pageSize);
        Page<Member> page = memberDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<Member> rows = page.getResult();


        return new PageResult(total,rows);
    }

    public void deleteByid(Integer id) {
        memberDao.deleteById(id);
    }

    public void edit(Member member) {
        memberDao.edit(member);
    }

    public Member findById(Integer id) {
       return memberDao.findById(id);
    }
}
