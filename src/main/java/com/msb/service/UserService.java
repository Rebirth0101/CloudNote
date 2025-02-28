package com.msb.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.msb.dao.UserDao;
import com.msb.po.User;
import com.msb.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.util.List;

public class UserService {
    private UserDao userDao=new UserDao();

    /**
     用户登录
         1. 判断参数是否为空
             如果为空
                设置ResultInfo对象的状态码和提示信息
                返回resultInfo对象
         2. 如果不为空，通过用户名查询用户对象
         3. 判断用户对象是否为空
             如果为空
                 设置ResultInfo对象的状态码和提示信息
                 返回resultInfo对象
         4. 如果用户对象不为空，将数据库中查询到的用户对象的密码与前台传递的密码作比较 （将密码加密后再比较）
             如果密码不正确
                 设置ResultInfo对象的状态码和提示信息
                 返回resultInfo对象
         5. 如果密码正确
            设置ResultInfo对象的状态码和提示信息
         6. 返回resultInfo对象
     * @param username
     * @param userPwd
     * @return
     */
    public ResultInfo<User> userLogin(String username, String userPwd) {
        ResultInfo<User> resultInfo=new ResultInfo();

        //数据回显
        User u=new User();
        u.setUname(username);
        u.setUpwd(userPwd);
        //设置到resultInfo对象中
        resultInfo.setResult(u);

        //1. 判断参数是否为空
        if (StrUtil.isBlank(username)||StrUtil.isBlank(userPwd)) {
            //   如果为空，设置ResultInfo对象的状态码和提示信息

            resultInfo.setCode(0);
            //返回resultInfo对象
            resultInfo.setMsg("用户姓名或密码不能为空");
            return resultInfo;
        }
        //2. 如果不为空，通过用户名查询用户对象
        List<User> list = userDao.queryUserByUserName(username);
        //判断用户对象是否为空
        if(list.size()==0){
            //如果为空,设置ResultInfo对象的状态码和提示信息
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名不存在");
            //返回resultInfo对象
            return resultInfo;
        } else if (list.size()>2) {
            //如果出现同名账户,设置ResultInfo对象的状态码和提示信息
            resultInfo.setCode(0);
            resultInfo.setMsg("出现同名账户，账号被锁定，请联系管理员处理");
            //返回resultInfo对象
            return resultInfo;
        }
        //4. 如果用户对象不为空，将数据库中查询到的用户对象的密码与前台传递的密码作比较 （将密码加密后再比较）
        //将前台传递的密码按照MD5算法的方式加密
        userPwd=DigestUtil.md5Hex(userPwd);
        //判断加密后的密码是否与数据库中的一致
        if(!userPwd.equals(list.get(0).getUpwd())){
            //如果为密码不正确,设置ResultInfo对象的状态码和提示信息
            resultInfo.setCode(0);
            resultInfo.setMsg("密码错误");
            //返回resultInfo对象
            return resultInfo;

        }

        //5. 如果密码正确,设置ResultInfo对象的状态码和提示信息
        resultInfo.setCode(1);
        resultInfo.setResult(list.get(0));
        //6. 返回resultInfo对象
        return resultInfo;
    }

    /**
     * 验证昵称的唯一性
         1. 判断昵称是否为空
         如果为空，返回"0"
         2. 调用Dao层，通过用户ID和昵称查询用户对象
         3. 判断用户对象存在
         存在，返回"0"
         不存在，返回"1"
     * @param nick
     * @param userId
     * @return
     */
    public Integer checkNick(String nick, Integer userId) {
        //1. 判断昵称是否为空
        if (StrUtil.isBlank(nick)){
            //如果为空，返回"0"
            return 0;
        }
        //2. 调用Dao层，通过用户ID和昵称查询用户对象
        User user=userDao.queryUserByNickNameAndUserId(nick,userId);
        //3. 判断用户对象存在
        if (user!=null){//存在，返回"0"
            return 0;
        }else {//不存在，返回"1"
            return 1;
        }


    }

    /**
     * 修改用户信息
         1. 获取参数（昵称、心情）
         2. 参数的非空校验（判断必填参数非空）
            如果昵称为空，将状态码和错误信息设置resultInfo对象中，返回resultInfo对象
         3. 从session作用域中获取用户对象（获取用户对象中默认的头像）
         4. 实现上上传文件
             1. 获取Part对象 request.getPart("name"); name代表的是file文件域的name属性值
             2. 通过Part对象获取上传文件的文件名
             3. 判断文件名是否为空
             4. 获取文件存放的路径  WEB-INF/upload/目录中
             5. 上传文件到指定目录
         5. 更新用户头像 （将原本用户对象中的默认头像设置为上传的文件名）
         6. 调用Dao层的更新方法，返回受影响的行数
         7. 判断受影响的行数
            如果大于0，则修改成功；否则修改失败
         8. 返回resultInfo对象
     * @param req
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest req) {
        ResultInfo<User> resultInfo=new ResultInfo<>();
        //1. 获取参数（昵称、心情）
        String nick=req.getParameter("nick");
        String mood=req.getParameter("mood");

        //2. 参数的非空校验（判断必填参数非空）
        if (StrUtil.isBlank(nick)){
            resultInfo.setCode(0);
            resultInfo.setMsg("昵称不能为空");
            return resultInfo;
        }

        //3. 从session作用域中获取用户对象（获取用户对象中默认的头像）
        User user = (User) req.getSession().getAttribute("user");
        //设置修改的昵称和头像
        user.setNick(nick);
        user.setMood(mood);

        //4. 实现上传文件
        try{
            //1. 获取Part对象 request.getPart("name"); name代表的是file文件域的name属性值
            Part part=req.getPart("img");
            //2. 通过Part对象获取上传文件的文件名（获取用户对象中默认的头像）
            String header = part.getHeader("Content-Disposition");
            System.out.println(header);
            // 设置修改的昵称和头像
            String str=header.substring(header.lastIndexOf("=")+2);
            // 获取上传的文件名
            String fileName=str.substring(0,str.length()-1);
            //3. 判断文件名是否为空
            if (!StrUtil.isBlank(fileName)){
                // // 如果用户上传了头像，则更新用户对象中的头像
                user.setHead(fileName);
                //4. 获取文件存放的路径  WEB-INF/upload/目录中
                String filePath=req.getSession().getServletContext().getRealPath("/WEB-INF/upload/");
                //5. 上传文件到指定目录
                part.write(filePath+"/"+fileName);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //6. 调用Dao层的更新方法，返回受影响的行数
        int row=userDao.updateUser(user);
        //7. 判断受影响的行数
        if (row>0){
            resultInfo.setCode(1);
            //更新session中的用户对象
            req.getSession().setAttribute("user",user);
        }else{
            resultInfo.setCode(0);
            resultInfo.setMsg("更新用户信息失败");
        }
        return resultInfo;
    }
}
