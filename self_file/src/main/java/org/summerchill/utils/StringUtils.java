package org.summerchill.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kxh
 * @description
 * @date 20210609_23:49
 */

@Slf4j
public class StringUtils {

    /**
     * 获取字符串中符合指定正则的部分
     * 比如获取http/hdfs的根目录
     * @param sourceStr
     * @param regExStr
     * @return
     */
    public static String getTargetStrUseRegEx(String sourceStr, String regExStr) {
        String targetStr = "";
        try {
            Pattern pattern = Pattern.compile(regExStr);
            Matcher matcher = pattern.matcher(sourceStr);
            if (matcher.find()) {
                targetStr = matcher.group();
                log.info("使用正则表达式找到目标字符串为:" + targetStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return targetStr;
    }


}
