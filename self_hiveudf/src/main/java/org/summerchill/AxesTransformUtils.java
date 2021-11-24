package org.summerchill;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 提供了百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换
 */
public class AxesTransformUtils {

    // ================================================================
    // Constants
    // ================================================================

    // 定义一些常量
    private static final double X_PI = Math.PI * 3000.0 / 180.0;

    // 克拉索夫斯基椭球参数长半轴
    private static final double RADIUS = 6378245.0;
    // 克拉索夫斯基椭球参数第一偏心率平方
    private static final double ECCENT = 0.00669342162296594323;

    // ================================================================
    // Constructors
    // ================================================================

    // 防止构造
    private AxesTransformUtils() {
    }

    // ================================================================
    // Public or Protected Methods
    // ================================================================

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     */
    public static LngLat bd09ToGcj02(double lng, double lat) {
        double x = lng - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double ggLng = z * Math.cos(theta);
        double ggLat = z * Math.sin(theta);
        return new LngLat(ggLng, ggLat);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     */
    public static LngLat gcj02ToBd09(double lng, double lat) {
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * X_PI);
        double bdLng = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new LngLat(bdLng, bdLat);
    }

    /**
     * WGS84转GCj02
     */
    public static LngLat wgs84ToGcj02(double lng, double lat) {
        if (outOfChina(lng, lat)) {
            return new LngLat(lng, lat);
        } else {
            double dLat = transformLat(lng - 105.0, lat - 35.0);
            double dLng = transformLng(lng - 105.0, lat - 35.0);
            double radLat = lat / 180.0 * Math.PI;
            double magic = Math.sin(radLat);
            magic = 1 - ECCENT * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((RADIUS * (1 - ECCENT)) / (magic * sqrtMagic) * Math.PI);
            dLng = (dLng * 180.0) / (RADIUS / sqrtMagic * Math.cos(radLat) * Math.PI);
            double mgLat = lat + dLat;
            double mgLng = lng + dLng;
            return new LngLat(mgLng, mgLat);
        }
    }

    /**
     * GCJ02 转换为 WGS84
     */
    public static LngLat gcj02ToWgs84(double lng, double lat) {
        if (outOfChina(lng, lat)) {
            return new LngLat(lng, lat);
        } else {
            double dLat = transformLat(lng - 105.0, lat - 35.0);
            double dLng = transformLng(lng - 105.0, lat - 35.0);
            double radLat = lat / 180.0 * Math.PI;
            double magic = Math.sin(radLat);
            magic = 1 - ECCENT * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLng = (dLng * 180.0) / (RADIUS / sqrtMagic * Math.cos(radLat) * Math.PI);
            dLat = (dLat * 180.0) / ((RADIUS * (1 - ECCENT)) / (magic * sqrtMagic) * Math.PI);
            double mgLat = lat + dLat;
            double mgLng = lng + dLng;
            return new LngLat(lng * 2 - mgLng, lat * 2 - mgLat);
        }
    }

    /**
     * 转换纬度
     */
    public static double transformLat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * Math.PI) + 320 * Math.sin(lat * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 转换经度
     */
    public static double transformLng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * Math.PI) + 40.0 * Math.sin(lng / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * Math.PI) + 300.0 * Math.sin(lng / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 判断是否在国内，不在国内则不做偏移
     */
    public static boolean outOfChina(double lng, double lat) {
        return (lng < 72.004 || lng > 137.8347) || (lat < 0.8293 || lat > 55.8271);
    }

    /**
     * 字符串转为列表,并把百度转为高度
     */
    public static List<LngLat> transformPolyline(String polyline) {

        List<LngLat> LngLats = Lists.newArrayList();

        if (StringUtils.isNotBlank(polyline)) {
            String[] split = StringUtils.split(polyline, ";");
            for (String item : split) {
                LngLat oldLngLat = new LngLat(item);
                LngLat LngLat = bd09ToGcj02(oldLngLat.getLng(), oldLngLat.getLat());
                LngLats.add(LngLat);
            }
        }
        return LngLats;
    }

    /**
     * 列表转为字符串
     */
    public static String transformString(List<LngLat> LngLats) {

        StringBuilder polyline = new StringBuilder("");

        if (CollectionUtils.isNotEmpty(LngLats)) {
            for (LngLat LngLat : LngLats) {
                polyline.append(LngLat.getLng());
                polyline.append(",");
                polyline.append(LngLat.getLat());
                polyline.append(";");
            }
            polyline.deleteCharAt(polyline.length() - 1);
        }
        return polyline.toString();
    }

    // ================================================================
    // Private Methods
    // ================================================================
}
