package org.summerchill;

public class ConstantUtils {
    public static final String BAIDU_MAP = "BAIDU";
    public static final String GAODE_MAP = "GAODE";
    public static final String BAIDU_MAP_CHN = "百度";
    public static final String GAODE_MAP_CHN = "高德";


    public static final String HIVE_DRIVERNAME = "org.apache.hive.jdbc.HiveDriver";
    //正式部署使用:
    public static final String HIVE_URL = System.getenv("HIVE_URL");
    public static final String HIVE_USERNAME = System.getenv("HIVE_USERNAME");
    public static final String HIVE_PASSWORD = System.getenv("HIVE_PASSWORD");
}
