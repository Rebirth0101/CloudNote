package com.msb.web;

import com.msb.po.Note;
import com.msb.po.NoteVo;
import com.msb.po.User;
import com.msb.service.NoteService;
import com.msb.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置首页导航高亮
        req.setAttribute("menu_page","index");

        //得到用户行为（判断是什么条件查询，标题查询、日期查询、类型查询）
        String actionName = req.getParameter("actionName");
        // 将用户行为设置到request作用域中 （分页导航中需要获取）
        req.setAttribute("action",actionName);
        // 判断用户行为
        if("searchTitle".equals(actionName)){
            // 得到查询条件：标题
            String title = new String(req.getParameter("title").getBytes("ISO-8859-1"),"UTF-8");
            // 将查询条件设置到request请求域中（查询条件的回显）
            req.setAttribute("title",title);

            //标题搜索
            noteList(req,resp,title,null,null);
        }else if("searchDate".equals(actionName)){
            // 得到查询条件：日期
            String date =new String(req.getParameter("date").getBytes("ISO-8859-1"),"UTF-8");
            // 将查询条件设置到request请求域中（查询条件的回显）
            req.setAttribute("date",date);
            //日期搜索
            noteList(req,resp,null,date,null);

        }else if ("searchType".equals(actionName)){//类型查询
            String typeId =req.getParameter("typeId");
            // 将查询条件设置到request请求域中（查询条件的回显）
            req.setAttribute("typeId",typeId);
            //类型查询
            noteList(req,resp,null,null,typeId);
        }
        else {
            //分页查询云记列表
            noteList(req,resp,null,null,null);
        }



        //设置首页动态包含的页面
        req.setAttribute("changePage", "note/list.jsp");
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    /**
     * 分页查询云记列表
         1. 接收参数 （当前页、每页显示的数量）
         2. 获取Session作用域中的user对象
         3. 调用Service层查询方法，返回Page对象
         4. 将page对象设置到request作用域中
     * @param req
     * @param resp
     * @param title 标题
     */
    private void noteList(HttpServletRequest req, HttpServletResponse resp, String title,String date,String typeId) throws ServletException, IOException {

        NoteService noteService=new NoteService();
        //1. 接收参数 （当前页、每页显示的数量）
        String pageNum = req.getParameter("pageNum");
        String pageSize = req.getParameter("pageSize");


        //2. 获取Session作用域中的user对象
        User user = (User) req.getSession().getAttribute("user");
        
        //3. 调用Service层查询方法，返回Page对象
        Page<Note> page=noteService.findNoteListByPage(pageNum,pageSize,user.getUserId(),title,date,typeId);
        //4. 将page对象设置到request作用域中
        req.setAttribute("page",page);

        //通过日期分组查询当前用户下的云记数量
        List<NoteVo> dataInfo=noteService.findNoteCountByDate(user.getUserId());
        //设置集存放在session作用域中
        req.getSession().setAttribute("dataInfo",dataInfo);

        //通过类型分组查询当前用户下的云记数量
        List<NoteVo> typeInfo=noteService.findNoteCountByType(user.getUserId());
        //设置集存放在session作用域中
        req.getSession().setAttribute("typeInfo",typeInfo);
    }
}
