package com.msb.dao;

import com.msb.po.NoteType;
import com.msb.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteTypeDao {
    /**
     通过用户ID查询类型集合
         1. 定义SQL语句
            String sql = "select typeId,typeName,userId from tb_note_type where userId = ? ";
         2. 设置参数列表
         3. 调用BaseDao的查询方法，返回集合
         4. 返回集合
     * @param userId
     * @return
     */
    public List<NoteType> findTypeListByUserId(Integer userId) {
        List<NoteType> list = null;
        //1. 定义SQL语句
        String sql = "select typeId,typeName,userId from tb_note_type where userId = ? ";
        //2. 设置参数列表
        List<Object> params = new ArrayList<>();
        params.add(userId);
        //3. 调用BaseDao的查询方法，返回集合
        list=BaseDao.queryRows(sql,params,NoteType.class);
        return list;

    }

    /**
     * 通过类型ID查询云记记录的数量，返回云记数量
     * @param typeId
     * @return
     */
    public long findNoteCountByTypeId(String typeId) {
        String sql = "select count(1) from tb_note where typeId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        long count= (long) BaseDao.querySingleValue(sql,params);
        return count;
    }

    /**
     * 通过类型ID删除指定的类型记录，返回受影响的行数
     * @param typeId
     * @return
     */
    public int deleteTypeById(String typeId) {
        String sql = "delete from tb_note_type where typeId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        int row=BaseDao.executeUpdate(sql,params);
        return row;
    }

    /**
     * 查询当前登录用户下，类型名称是否唯一
     *     返回1，表示成功
     *     返回0，表示失败
     * @param typeName
     * @param userId
     * @param typeId
     * @return
     */
    public Integer checkTypeName(String typeName, Integer userId, String typeId) {
        String sql = "select * from tb_note_type where typeName = ? and userId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(typeName);
        params.add(userId);
        NoteType noteType= (NoteType) BaseDao.queryRow(sql,params,NoteType.class);
        //如果为空，表示可用
        if(noteType==null){
            return 1;
        }else {
            // 如果是修改操作，则需要判断是否是当前记录本身
            if (typeId.equals(noteType.getTypeId().toString())) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 添加方法，返回主键
     * @param typeName
     * @param userId
     * @return
     */
    public Integer addType(String typeName, Integer userId) {
        Integer key=null;
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try {
            //获取数据库连接
            connection=DBUtil.getConnection();
            //定义sql语句
            String sql="insert into tb_note_type(typeName,userId) values(?,?)";
            //预编译
            preparedStatement=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //设置参数
            preparedStatement.setString(1,typeName);
            preparedStatement.setInt(2,userId);
            //执行更新，返回受影响行数
            int row=preparedStatement.executeUpdate();
            if(row>0){
                //获取返回主键的结果集
                resultSet=preparedStatement.getGeneratedKeys();
                //得到主键的值
                if(resultSet.next()){
                    key=resultSet.getInt(1);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return key;
    }

    /**
     * 修改方法，返回受影响的行数
     * @param typeId
     * @param typeName
     * @return
     */
    public Integer updateType(String typeId, String typeName) {
        String sql = "update tb_note_type set typeName = ? where typeId = ? ";
        List<Object> params = new ArrayList<>();
        params.add(typeName);
        params.add(typeId);
        int row=BaseDao.executeUpdate(sql,params);
        return row;
    }
}
