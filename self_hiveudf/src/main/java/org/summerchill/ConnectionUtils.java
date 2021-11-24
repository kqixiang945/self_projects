package org.summerchill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {
    static Connection hiveConn = null;

    /**
     * 获取到jdbc对Hive的连接
     *
     * @return
     */
    public static Connection loadHiveConnection() {
        try {
            Class.forName(ConstantUtils.HIVE_DRIVERNAME);
            hiveConn = DriverManager.getConnection(ConstantUtils.HIVE_URL, ConstantUtils.HIVE_USERNAME, ConstantUtils.HIVE_PASSWORD);
            return hiveConn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
