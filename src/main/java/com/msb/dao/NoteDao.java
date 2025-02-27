package com.msb.dao;

import cn.hutool.core.util.StrUtil;
import com.msb.po.Note;
import com.msb.po.NoteVo;

import java.util.ArrayList;
import java.util.List;

public class NoteDao {
    /**
     * 添加或修改云记,返回受影响的行数

     * @param note
     * @return
     */
    public int addOrUpdate(Note note) {
        String sql="";
        List<Object> params=new ArrayList<Object>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        //判断noteId是否为空，如果为空，则为添加操作，如果不为空，则为修改操作
        if (note.getNoteId()==null){//添加操作
            sql="insert into tb_note (typeId,title,content,pubTime,lon,lat) values (?,?,?,now(),?,?)";
            params.add(note.getLon());
            params.add(note.getLat());
        }else {//修改操作
            sql="update tb_note set typeId=?,title=?,content=? where noteId=?";
            params.add(note.getNoteId());
        }

        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }

    /**
     *  查询当前登录用户的云记数量，返回总记录数
     * @param userId
     * @return
     */
    public long findNoteCount(Integer userId,String title,String date,String typeId) {
        String sql="select count(1) from tb_note n inner join tb_note_type t on n.typeId=t.typeId where userId=?";
        List<Object> params=new ArrayList<>();
        params.add(userId);

        // 判断条件查询的参数是否为空 （如果查询的参数不为空，则拼接sql语句，并设置所需要的参数
        if (!StrUtil.isBlank(title)){
            //拼接sql语句
            sql+=" and title like concat('%',?,'%')";
            //设置所需要的参数
            params.add(title);
        }else if (!StrUtil.isBlank(date)){// 日期查询
            //拼接sql语句
            sql+=" and DATE_FORMAT(pubTime,'%Y年%m月') = ?";
            //设置所需要的参数
            params.add(date);
        }else if (!StrUtil.isBlank(typeId)){// 日期查询
            //拼接sql语句
            sql+=" and n.typeId = ?";
            //设置所需要的参数
            params.add(typeId);
        }
        long count= (long) BaseDao.querySingleValue(sql,params);
        return count;
    }

    /**
     * 查询当前登录用户下当前页的数据列表，返回note集合
     * @param userId
     * @param index
     * @param pageSize
     * @return
     */
    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize, String title,String date,String typeId) {
        String sql="select noteId,title,pubTime from tb_note n inner join tb_note_type t on n.typeId=t.typeId where userId=? ";
        List<Object> params=new ArrayList<>();
        params.add(userId);

        // 判断条件查询的参数是否为空 （如果查询的参数不为空，则拼接sql语句，并设置所需要的参数
        if (!StrUtil.isBlank(title)){//标题查询
            //拼接sql语句
            sql+=" and title like concat('%',?,'%')";
            //设置所需要的参数
            params.add(title);
        }else if (!StrUtil.isBlank(date)){// 日期查询
            //拼接sql语句
            sql+=" and DATE_FORMAT(pubTime,'%Y年%m月') = ?";
            //设置所需要的参数
            params.add(date);
        }else if (!StrUtil.isBlank(typeId)){// 日期查询
            //拼接sql语句
            sql+=" and n.typeId = ?";
            //设置所需要的参数
            params.add(typeId);
        }

        // 拼接分页的sql语句 （limit语句需要写在sql语句最后）
        sql+=" order by pubTime desc limit ?,?";
        params.add(index);
        params.add(pageSize);

        List<Note> noteList=BaseDao.queryRows(sql,params,Note.class);
        return noteList;
    }

    /**
     * 通过日期分组查询当前用户下的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByDate(Integer userId) {
        String sql="SELECT count(1) noteCount,DATE_FORMAT(pubTime,'%Y年%m月') groupName FROM `tb_note` n \n" +
                "JOIN tb_note_type t\n" +
                "on n.typeId=t.typeId\n" +
                "WHERE userId=?\n" +
                "GROUP BY groupName\n" +
                "ORDER BY groupName desc";
        List<Object> params=new ArrayList<>();
        params.add(userId);
        List<NoteVo> list=BaseDao.queryRows(sql,params,NoteVo.class);
        return list;
    }

    /**
     * 通过类型分组查询当前用户下的云记数量
     * @param userId
     * @return
     */
    public List<NoteVo> findNoteCountByType(Integer userId) {
        String sql="SELECT COUNT(noteId) noteCount,t.typeId,typeName groupName FROM tb_note n \n" +
                "RIGHT JOIN tb_note_type t\n" +
                "on n.typeId=t.typeId\n" +
                "WHERE userId=?\n" +
                "GROUP BY t.typeId\n" +
                "ORDER BY COUNT(noteId) desc\n";
        List<Object> params=new ArrayList<>();
        params.add(userId);
        List<NoteVo> list=BaseDao.queryRows(sql,params,NoteVo.class);
        return list;
    }

    /**
     * 通过noteId查询note对象
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        String sql="select noteId,title,content,n.typeId,pubTime,typeName from tb_note n inner join tb_note_type t on n.typeId=t.typeId where noteId=?";
        List<Object> params=new ArrayList<>();
        params.add(noteId);
        Note note= (Note) BaseDao.queryRow(sql,params,Note.class);
        return note;
    }

    /**
     * 通过noteId删除云记记录，返回受影响的行数
     * @param noteId
     * @return
     */
    public int deleteNoteById(String noteId) {
        String sql="delete from tb_note where noteId=?";
        List<Object> params=new ArrayList<>();
        params.add(noteId);
        int row = BaseDao.executeUpdate(sql,params);
        return row;
    }

    /**
     * 查询用户发布云记时的坐标
     * @param userId
     * @return
     */
    public List<Note> queryNoteList(Integer userId) {
        String sql="select lon,lat from tb_note n inner join tb_note_type t on n.typeId=t.typeId where userId=?";

        List<Object> params=new ArrayList<>();
        params.add(userId);

        List<Note> noteList=BaseDao.queryRows(sql,params,Note.class);

        return noteList;
    }
}
