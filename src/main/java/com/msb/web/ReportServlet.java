package com.msb.web;

import com.msb.po.Note;
import com.msb.po.User;

import com.msb.service.NoteService;
import com.msb.util.JsonUtil;
import com.msb.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private NoteService noteService = new NoteService();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //设置首页导航栏地高亮值
        req.setAttribute("menu_page","report");

        //得到用户行为
        String actionName=req.getParameter("actionName");

        //判断用户行为
        if ("info".equals(actionName)){
            //进入报表页面
            reportInfo(req,resp);
        }else if ("month".equals(actionName)){
            //通过月份查询对应的云记数量
            queryNoteCountByMonth(req,resp);

        }else if ("location".equals(actionName)){
            //查询用户发布云记时的坐标
            queryNoteLotAndLat(req,resp);
        }
    }

    /**
     * 查询用户发布云记时的坐标
     * @param req
     * @param resp
     */
    private void queryNoteLotAndLat(HttpServletRequest req, HttpServletResponse resp) {
        //从session中获取当前用户对象
        User user=(User)req.getSession().getAttribute("user");
        // 调用Service层的查询方法，返回ResultInfo对象
        ResultInfo<List<Note>> resultInfo=noteService.queryNoteLotAndLat(user.getUserId());
        // 将ResultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
        JsonUtil.toJson(resp,resultInfo);

    }

    /**
     * 通过月份查询对应的云记数量
     * @param req
     * @param resp
     */
    private void queryNoteCountByMonth(HttpServletRequest req, HttpServletResponse resp) {
        //从session中获取当前用户对象
        User user=(User)req.getSession().getAttribute("user");
        // 调用Service层的查询方法，返回ResultInfo对象
        ResultInfo<Map<String,Object>> resultInfo=noteService.queryNoteCountByMonth(user.getUserId());
        // 将ResultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
        JsonUtil.toJson(resp,resultInfo);
    }

    /**
     * 进入报表页面
     * @param req
     * @param resp
     */
    private void reportInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置首页动态包含的页面值
        req.setAttribute("changePage","report/info.jsp");
        //请求转发跳转到index.jsp
        req.getRequestDispatcher("index.jsp").forward(req,resp);
        System.out.println();
    }
}
