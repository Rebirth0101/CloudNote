package com.msb.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 将对象转换成JOSN格式的字符串，响应给ajax的回调函数
 */
public class JsonUtil {
    public static void toJson(HttpServletResponse resp,Object resultInfo) {
        try {
            //设置响应类型及编码格式（json类型）
            resp.setContentType("application/json;charset=utf-8");
            //得到字符数输出流
            PrintWriter out=resp.getWriter();
            //通过fastjson的方法，将ResultInfo对象转换成JSON格式的字符串
            String json = JSON.toJSONString(resultInfo);
            //通过输出流输出JSON格式的字符串
            out.print(json);
            //关闭资源
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
