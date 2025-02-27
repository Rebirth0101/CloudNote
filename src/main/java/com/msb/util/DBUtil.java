package com.msb.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtil {
    //加载配置文件
    private static Properties properties=new Properties();

    static {
        try {
            //加载配置文件（输入流）
            InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            //通过load()方法将输入流的内容加载到配置文件对象中
            properties.load(is);
            //通过配置文件对象的getProperty()方法获取驱动名，并加载对象
            Class.forName(properties.getProperty("jdbcName"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection() {
        Connection connection=null;

        try {
            //获取数据库连接的相关信息
            String dbUrl = properties.getProperty("dbUrl");
            String dbName = properties.getProperty("dbName");
            String dbPwd = properties.getProperty("dbPwd");
            //得到数据库连接
            connection=DriverManager.getConnection(dbUrl,dbName,dbPwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭资源
     * @param resultSet
     * @param preparedStatement
     * @param connection
     */
    public static void close(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet) {
        //判断资源对象如果不为空，则关闭
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
