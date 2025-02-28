package com.msb.dao;

import com.msb.po.User;
import com.msb.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    /**
     * 通过用户名查询用户对象
     *  1. 定义sql语句
     *  2. 设置参数集合
     *  3. 调用BaseDao的查询方法
     * @param userName
     * @return
     */
    public List<User> queryUserByUserName(String userName){
        // 1. 定义sql语句
        String sql = "select * from tb_user where uname = ?";
        //2. 设置参数集合
        List<Object> params = new ArrayList<Object>();
        params.add(userName);
        //3. 调用BaseDao的查询方法
        List<User> list=BaseDao.queryRows(sql,params, User.class);
        return list;
    }

    /**
     通过用户名查询用户对象， 返回用户对象
         1. 获取数据库连接
         2. 定义sql语句
         3. 预编译
         4. 设置参数
         5. 执行查询，返回结果集
         6. 判断并分析结果集
         7. 关闭资源
     * @param userName
     * @return
     */
    public User queryUserByUserName02(String userName) {
        User user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1. 获取数据库连接
            conn = DBUtil.getConnection();
            // 2. 定义sql语句
            String sql = "select * from tb_user where uname = ?";
            // 3. 预编译
            ps = conn.prepareStatement(sql);
            // 4. 设置参数
            ps.setString(1, userName);
            // 5. 执行查询，返回结果集
            rs = ps.executeQuery();
            // 6. 判断并分析结果集
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("userid"));
                user.setUname(userName);
                user.setHead(rs.getString("head"));
                user.setMood(rs.getString("mood"));
                user.setNick(rs.getString("nick"));
                user.setUpwd(rs.getString("upwd"));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //7. 关闭资源
            DBUtil.close(conn,ps,rs);
        }
        return user;
    }

    /**
     * 通过昵称和用户id查询用户对象
         1. 定义SQL语句
         通过用户ID查询除了当前登录用户之外是否有其他用户使用了该昵称
         指定昵称  nick （前台传递的参数）
         当前用户  userId （session作用域中的user对象）
         String sql = "select * from tb_user where nick = ? and userId != ?";
         2. 设置参数集合
         3. 调用BaseDao的查询方法
     * @param nick
     * @param userId
     * @return
     */
    public User queryUserByNickNameAndUserId(String nick, Integer userId) {
        //1. 定义SQL语句
        String sql = "select * from tb_user where nick = ? and userId != ?";
        //2. 设置参数集合
        List<Object> params=new ArrayList<>();
        params.add(nick);
        params.add(userId);
        //3. 调用BaseDao的查询方法
        User user= (User) BaseDao.queryRow(sql,params,User.class);
        return user;
    }

    /**
     * 通过用户ID修改用户信息
         1. 定义SQL语句
            String sql = "update tb_user set nick = ?, mood = ?, head = ? where userId = ? ";
         2. 设置参数集合
         3. 调用BaseDao的更新方法，返回受影响的行数
         4. 返回受影响的行数
     * @param user
     * @return
     */
    public int updateUser(User user) {
        //1. 定义SQL语句
        String sql = "update tb_user set nick = ?, mood = ?, head = ? where userId = ? ";
        //2. 设置参数集合
        List<Object> params=new ArrayList<>();
        params.add(user.getNick());
        params.add(user.getMood());
        params.add(user.getHead());
        params.add(user.getUserId());
        //3. 调用BaseDao的更新方法，返回受影响的行数
        int row = BaseDao.executeUpdate(sql,params);
        return row;
    }
}
