package org.summerchill;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class LngLat implements Serializable {

    // 经度信息
    // 维度信息
    private double lng;
    private double lat;

    /**
     * 根据中心点初始化
     */
    public LngLat(String location) {
        String[] split = StringUtils.split(location, ",");
        this.lng = Double.parseDouble(split[0]);
        this.lat = Double.parseDouble(split[1]);
    }

    /**
     * 根据中心点初始化
     */
    public LngLat(double lng,double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LngLat lngLat = (LngLat) o;

        return Double.compare(lngLat.lng, lng) == 0 && Double.compare(lngLat.lat, lat) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCenter() {
        return this.lng + "," + this.lat;
    }
}