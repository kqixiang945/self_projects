package org.example.ck;

import java.sql.*;

/**
 * jdbc search clickhouse
 *
 */
public class CkJdbcUtils {
    public static void main(String[] args) throws Exception {
        Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
        String url = "jdbc:clickhouse://cc-2zem5hghge5cnrou4.ads.rds.aliyuncs.com:8123/default";
        String username = "dbadmin";
        String password = "US@Vsov#L86vug*ji46oZovXuXpyn";
        Connection con = DriverManager.getConnection(url, username, password);
        Statement stmt = con.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from c5_zszhkbf36_ach_zushou_newbi_cust_inten_detail");
        while (resultSet.next()) {
            String id = resultSet.getString("USER_ID_JL");
            String name = resultSet.getString("USER_NAME_JL");
            System.out.println(id + ":" + name);
        }
        con.close();
        stmt.close();
        resultSet.close();
    }
}
