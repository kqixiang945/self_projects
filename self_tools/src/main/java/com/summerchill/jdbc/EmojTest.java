package com.summerchill.jdbc;

import java.sql.*;
/**
 * @author kxh
 * @description
 * @date 20210220_16:33
 */
public class EmojTest {

    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://rm-2zem01x8l765c95nn.mysql.rds.aliyuncs.com:3306/sit_analysis_bigdata?characterEncoding=utf-8";
    static final String DB_URL = "jdbc:mysql://rm-2zem01x8l765c95nn.mysql.rds.aliyuncs.com:3306/sit_analysis_bigdata?characterEncoding=utf-8&yearIsDateType=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false&rewriteBatchedStatements=true";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "sit_analysis_bigdata_user";
    static final String PASS = "go2j3JjS4QZK0UUU";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "insert into  a34_wain_order (id,customer_idea) values (6666000,'\uD83D\uDD78')";
            //sql = "insert into  a34_wain_order (id,inform_user_name) values ('666666','kxh')";
            stmt.execute(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}
