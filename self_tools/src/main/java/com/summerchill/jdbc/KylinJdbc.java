package com.summerchill.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * kylin使用jdbc连接测试类:
 * 官方文档 http://kylin.apache.org/cn/docs/tutorial/jdbc.html
 * <p>
 * jdbc:kylin://<hostname>:<port>/<kylin_project_name>
 * 如果“ssl”为true，“port”应该是Kylin server的HTTPS端口。
 * 如果“port”未被指定，driver会使用默认的端口：HTTP 80，HTTPS 443。
 * 必须指定“kylin_project_name”并且用户需要确保它在Kylin server上存在。
 */
public class KylinJdbc {
    public static void main(String[] args) throws Exception {
        // 加载Kylin的JDBC驱动程序
        Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();

        // 配置登录Kylin的用户名和密码
        Properties info = new Properties();
        info.put("user", "kongxiaohan");
        info.put("password", "YgYVXJt1L!bV8mm6");
        //info.put("user", "c4_st");
        //info.put("password", "1qaz@WSX");

        // 连接Kylin服务   http://10.9.192.11:17070
        Connection conn = driver.connect("jdbc:kylin://10.9.192.11:17070/C2_Data_service", info);
        //Connection conn = driver.connect("jdbc:kylin://10.9.192.11:17070/C4_ST_Hive", info);
        Statement state = conn.createStatement();
        //这个地方可以使用库名dim 也可以不使用dim库名.
        ResultSet resultSet = state.executeQuery("select * from dim.pub_summerchill_date");//✔
        //ResultSet resultSet = state.executeQuery("select * from dws.f18_c4_sfl_report_detail_new");
        //ResultSet resultSet = state.executeQuery("select * from dws.F05_CHANGE_TIME");//×
        //ResultSet resultSet = state.executeQuery("select * from dim.d05_shop_info");//✔️
        //ResultSet resultSet = state.executeQuery("select * from A05_INDEX_KYLIN");//×

        while (resultSet.next()) {
            String col1 = resultSet.getString(1);
            //String col2 = resultSet.getString(2);
            //String col3 = resultSet.getString(3);
            //System.out.println(col1 + "\t" + col2 + "\t" + col3);
            System.out.println(col1);
        }
    }
}