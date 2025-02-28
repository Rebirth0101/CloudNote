package com.msb.service;

import cn.hutool.core.util.StrUtil;
import com.msb.dao.NoteTypeDao;
import com.msb.po.NoteType;
import com.msb.vo.ResultInfo;

import java.util.List;

public class NoteTypeService {
    private NoteTypeDao noteTypeDao=new NoteTypeDao();

    /**
     通过用户ID查询类型集合
         1. 调用Dao层的查询方法，通过用户ID查询类型集合
         2. 返回类型集合
     * @param userId
     * @return
     */
    public List<NoteType> findTypeListByUserId(Integer userId){
        List<NoteType> typeList=noteTypeDao.findTypeListByUserId(userId);
        return typeList;
    }

    /**
     * 删除类型
         1. 判断参数是否为空
         2. 调用Dao层，通过类型ID查询云记记录的数量
         3. 如果云记数量大于0，说明存在子记录，不可删除
            code=0，msg="该类型存在子记录，不可删除"，返回resultInfo对象
         4. 如果不存在子记录，调用Dao层的更新方法，通过类型ID删除指定的类型记录，返回受影响的行数
         5. 判断受影响的行数是否大于0
            大于0，code=1；否则，code=0，msg="删除失败"
         6. 返回ResultInfo对象
     * @param typeId
     * @return
     */
    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo=new ResultInfo();
        //1. 判断参数是否为空
        if (StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常，请重试");
            return resultInfo;
        }
        //2. 调用Dao层，通过类型ID查询云记记录的数量
        long noteCount=noteTypeDao.findNoteCountByTypeId(typeId);
        //3. 如果云记数量大于0，说明存在子记录，不可删除
        if (noteCount>0){
            resultInfo.setCode(0);
            resultInfo.setMsg("该记录存在子记录，不可删除！！");
            return resultInfo;
        }

        //4. 如果不存在子记录，调用Dao层的更新方法，通过类型ID删除指定的类型记录，返回受影响的行数
        int row=noteTypeDao.deleteTypeById(typeId);
        //5. 判断受影响的行数是否大于0
        if (row>0){
            resultInfo.setCode(1);
        }else{
            resultInfo.setCode(0);
            resultInfo.setMsg("删除失败！");
        }
        return resultInfo;
    }

    /**
     *添加或修改类型
         1. 判断参数是否为空 （类型名称）
         如果为空，code=0，msg=xxx，返回ResultInfo对象
         2. 调用Dao层，查询当前登录用户下，类型名称是否唯一，返回0或1
         如果不可用，code=0，msg=xxx，返回ResultInfo对象
         3. 判断类型ID是否为空
         如果为空，调用Dao层的添加方法，返回主键 （前台页面需要显示添加成功之后的类型ID）
         如果不为空，调用Dao层的修改方法，返回受影响的行数
         4. 判断 主键/受影响的行数 是否大于0
         如果大于0，则更新成功
         code=1，result=主键
         如果不大于0，则更新失败
         code=0，msg=xxx
     * @param typeName
     * @param userId
     * @param typeId
     * @return
     */
    public ResultInfo<Integer> addOrUpdate(String typeName, Integer userId, String typeId) {
        ResultInfo<Integer> resultInfo=new ResultInfo();
        //1. 判断参数是否为空 （类型名称）
        if (StrUtil.isBlank(typeName)){
            //如果为空，code=0，msg=xxx，返回ResultInfo对象
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称不能为空");
            return resultInfo;
        }
        //2. 调用Dao层，查询当前登录用户下，类型名称是否唯一，返回0或1
        Integer code=noteTypeDao.checkTypeName(typeName,userId,typeId);
        if (code==0){
            //如果不可用，code=0，msg=xxx，返回ResultInfo对象
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在，请重新输入！");
            return resultInfo;
        }
        //3. 判断类型ID是否为空
        //返回的结果
        Integer key=null;//主键或受影响的行数
        if (StrUtil.isBlank(typeId)){
            //如果为空，调用Dao层的添加方法，返回主键 （前台页面需要显示添加成功之后的类型ID）
            key=noteTypeDao.addType(typeName,userId);
        }else{
            //如果不为空，调用Dao层的修改方法，返回受影响的行数
            key=noteTypeDao.updateType(typeId,typeName);

        }

        //4. 判断 主键/受影响的行数 是否大于0
        if (key>0){
            //如果大于0，则更新成功
            resultInfo.setCode(1);
            resultInfo.setResult(key);
        }else{
            ////如果不大于0，则更新失败
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
        }
        return resultInfo;
    }
}
