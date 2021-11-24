package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ParseJsonUtil {
    public static List<String> jsonTreeList = new ArrayList<String>();

    public static List<String> recJson(JsonElement element, String keyname, String START_JSON_PATH) {
        //处理一个JSONTree中的节点 判断这个节点下的一级节点 是不是 有JSONArray
        if (element == null || element.isJsonNull()) {
            if (keyname.contains(START_JSON_PATH)) {
                jsonTreeList.add(keyname + "\t" + "null");
            }
            //return null;
        } else if (element.isJsonArray()) {
            //解析JsonArray数组
            JsonArray jsonArray = element.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.size() > 0) {
                    JsonElement jsonElement = jsonArray.get(i);
                    recJson(jsonElement, keyname + "[" + i + "]", START_JSON_PATH);
                }
            }
        } else if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            for (Entry entry : jsonObject.entrySet()) {
                String name = (String) entry.getKey();
                JsonElement jsonElement = (JsonElement) entry.getValue();
                recJson(jsonElement, keyname + "." + name, START_JSON_PATH);
            }
        } else if (element.isJsonPrimitive()) {
            if (keyname.contains(START_JSON_PATH)) {
                JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();
                jsonTreeList.add(keyname + "\t" + element.getAsString());
            }
            //return sb.toString();
        }
        return jsonTreeList;
    }
}