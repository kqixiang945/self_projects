package org.example;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author kxh
 * @description
 * @date 20210609_23:39
 */
public class JsonUtils {
    /**
     * json string 转换为 map 对象
     *
     * @param jsonObjStr
     * @return
     */
    public static Map<String, String> jsonToMap(String jsonObjStr) {
        Map map = (Map) JSON.parse(jsonObjStr);
        return map;
    }

}
