package org.summerchill;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.awt.geom.GeneralPath;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Description(name = "location2plate_code",
        value = "_FUNC_(longitude,latitude,mapType,cityCode) - Returns the plate_code of the given longitude and latitude",
        extended = "longitude and latitude is the location of a place and data type is double;\n "
                + " mapType is gaode or baidu and you can use chinese 高德 or 百度; the default is gaode and data type is string\n"
                + " cityCode is the field adcode can limit the result set size of target table dim.pub_realestate_plate and data type is string; \n"
                + "Example:\n "
                + "  > SELECT _FUNC_(102.569625,24.927107,'baidu','') ;\n" + "  153933141624731454,115649093674312019,153933141624731454,115649093674312019")
public class UDFLocationToLandInfo extends UDF {
//public class UDFLocationToLandInfo {

    public Text evaluate(double longitude, double latitude, String mapType, String cityCode) {
        //首先校验 参数是否合法
        if ("".equals(longitude) || "".equals(latitude)){
            System.out.println("地点坐标不能为空!");
            return new Text();
        }
        //@Test
        //public void evaluate() {
        //    double longitude = 102.569625; double latitude = 24.927107; String mapType = "baidu"; String cityCode = "";
        try {
            LngLat lngLat = null;
            //如果是百度地图坐标需要转换
            if (ConstantUtils.BAIDU_MAP.equalsIgnoreCase(mapType) || ConstantUtils.BAIDU_MAP_CHN.equalsIgnoreCase(mapType)) {
                lngLat = AxesTransformUtils.bd09ToGcj02(longitude, latitude);
            } else {//其他的就默认是高德类型
                lngLat = new LngLat(longitude, latitude);
            }

            //加载Hive数据源.
            Connection hiveConnection = ConnectionUtils.loadHiveConnection();
            Statement statement = hiveConnection.createStatement();
            StringBuilder sqlSb = new StringBuilder("select plate_code, polyline from dim.pub_realestate_plate ");
            //adcode 如果不为空 就查询缩小范围...
            if (!"".equals(cityCode)) {
                sqlSb.append("where adcode = '" + cityCode + "'");
            } else {//查询全部
                //不做操作....
            }
            ResultSet resultSet = statement.executeQuery(sqlSb.toString());
            List<String> plateCodeList = new ArrayList<>();
            List<String> polyLineList = new ArrayList<>();
            while (resultSet.next()) {
                plateCodeList.add(resultSet.getString("plate_code"));
                polyLineList.add(resultSet.getString("polyline"));
            }
            //最后该函数返回的集合
            List plate_code_result_List = new ArrayList();
            //循环遍历polyLineList 看目标点位是不是在改范围内
            for (int i = 0; i < polyLineList.size(); i++) {
                String polyLine = polyLineList.get(i);
                String[] lon_lat_arr = StringUtils.split(polyLine, ";");
                List<LngLat> points = new ArrayList<>();
                for (String lon_lat : lon_lat_arr) {
                    points.add(new LngLat(lon_lat));
                }
                boolean exitFlag = checkWithJdkGeneralPath(points, lngLat);

                if (exitFlag) {
                    plate_code_result_List.add(plateCodeList.get(i));
                }
            }
            String resutStr = Joiner.on(",").join(plate_code_result_List);
            //System.out.println("最终返回的plate_code字符串为:" + resutStr);
            return new Text(resutStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回一个点是否在一个多边形区域内
     *
     * @param points 板块polyline多gps个点拆分后的list组成的多边形
     * @param point  需要校验的点gps
     */
    public static boolean checkWithJdkGeneralPath(List<LngLat> points, LngLat point) {
        GeneralPath p = new GeneralPath();

        LngLat first = points.get(0);
        p.moveTo(first.getLng(), first.getLat());
        for (int i = 1; i < points.size(); i++) {
            p.lineTo(points.get(i).getLng(), points.get(i).getLat());
        }

        p.lineTo(first.getLng(), first.getLat());
        p.closePath();
        return p.contains(new java.awt.geom.Point2D.Double(point.getLng(), point.getLat()));
    }
}
