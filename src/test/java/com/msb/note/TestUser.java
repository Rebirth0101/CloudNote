package com.msb.note;

import com.msb.dao.BaseDao;
import com.msb.dao.UserDao;
import com.msb.po.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestUser {
    @Test
    public void testQueryUserByUserName(){
        UserDao userDao = new UserDao();
        List<User> list = userDao.queryUserByUserName("admin");
        if (list.size()>0){
            System.out.println("出现同名账户");
        }else {
            System.out.println(list.get(0).getUpwd());
        }
    }
    @Test
    public void testAddUser(){
        UserDao userDao = new UserDao();
        String sql="insert into tb_user(uname,upwd,nick,head,mood) values(?,?,?,?,?)";
        //2. 设置参数集合
        List<Object> params = new ArrayList<Object>();
        params.add("zhangsan");
        params.add("e10adc3949ba59abbe56e057f20f883e");
        params.add("zhangsan");
        params.add("404.jpg");
        params.add("Hello");

        int row= BaseDao.executeUpdate(sql,params);
        System.out.println(row);
    }
}
