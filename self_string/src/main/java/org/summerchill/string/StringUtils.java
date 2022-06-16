package org.summerchill.string;


import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.CharSink;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;


/**
 * 个人在工作中总结的字符串
 */
@Slf4j
public class StringUtils {
    //datax源码中,匹配数字和字母下划线的多个字符,必须有$ ,可以没有{ 和 }
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("(\\$)\\{?(\\w+)\\}?");
    private static String[] SHELL_ESCAPE_CHARACTERS_ARRAY = {"~", "`", "#", "$", "&", "*", "(", ")", "\\", "|", "[", "]", "{", "}", ";", "'", "<", ">", "/", "?", "!", "<\">"};

    public static void main(String[] args) throws Exception {
        //1.获取字符串中的变量(${***} 或者 $*** 两种类型的)
        getVariables("字符串");

        //2.从一个key=value形式的文件内容中组织成一个Map(key不能一样)
        Map<String, String> keyValueMap = getKeyValueMapFromFile("properties文件的内容...");

        //3.删除一个内容字符串中指定开头的行.
        String newContent = removeScriptComment("文件内容...", "#");

        //4.删除List结合中没有用的元素(去除List中的空,\r和\n三种元素...多用于处理脚本文件中通过split切割";"出来的list)
        removeUselessStringInList(new ArrayList<String>());

        //5.把一个集合中的元素,通过指定的分隔符连接起来
        String newString = flattenCollectionToString(new ArrayList<String>(), ",");

        //6.处理文件格式内容,把Windows格式的文件输出成为Unix(主要是替换掉\r)
        String newString2 = handleStringFormat2Unix("字符串内容");

        //7.通过正则表达式的方式获取字符串中指定字符的个数
        getTargetCharNum("字符串内容...", "要匹配的正则表达式");

        //8.对字符串进行MD5加密
        System.out.println(MD5("9y9r_YSDXD"));

        //9.获取某个hdfs路径中的根目录
        System.out.println(getTargetStrUseRegEx("hdfs://cdhservice01/user/hive/warehouse/app.db/param_longzhu_hd_dq_project", "hdfs://([A-Za-z0-9_.:]+)"));

        //10.非可见字符转换成字符串,然后放入到一个文件中(直接打印店到控制台打印出来的内容可能对于这些非可见字符显示不准确).
        char ctrlA = 0x1;
        System.out.println(getNonVisibleChar(ctrlA));

        //11.字符串转换成为字符数组(建议以后这种直接设置StandardCharsets.UTF_8,否则不加编码设置遇到中文就会乱码.
        String s = "some text here";
        byte[] b = s.getBytes(StandardCharsets.UTF_8);

        //12.字符数组转换成字符串
        byte[] bytesArr = {(byte) 99, (byte)97, (byte)116};
        String bytesArrStr = new String(b, StandardCharsets.US_ASCII);

        //13.generate uuid


        //14.remove nonvisuable char
        removeInvisibleChars("tweewewe");



    }

    /**
     * remove a string first and last char
     * @param str
     * @return
     */
    public static String removeFirstandLast(String str) {
        if(!Strings.isNullOrEmpty(str)){
            // Removing first and last character of a string using substring() method
            str = str.substring(1, str.length() - 1);
            // Return the modified string
            return str;
        } else{
            return "";
        }
    }

    public static String removeInvisibleChars(String text) {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");
        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");
        return text.trim();
    }


    /**
     * 非可见字符转换成普通字符串(主要就是使用的非可见字符的ASCII码,可以用各种进制的表示)
     * 输出到一个文件中
     * 相关笔记总结: https://www.notion.so/hive-42458e7a1ec345338aa673906a9fd69d
     *
     * @return
     */
    public static String getNonVisibleChar(char character) throws IOException {
        String charStr = Character.toString(character);
        //把文件输出到
        String filePath = System.getProperty("java.io.tmpdir") + File.separator + "notVisualSymbol.txt";
        System.out.println("filePath:" + filePath);
        CharSink scriptSink = com.google.common.io.Files.asCharSink(new File(filePath), Charsets.UTF_8);
        scriptSink.write(charStr);
        return charStr;
    }

    /**
     * 获取字符串中"$***"的字符串变量
     *
     * @param processContent
     * @return
     */
    public static Set<String> getVariables(final String processContent) {
        Set<String> variavleSet = new HashSet<>();
        Matcher matcher = VARIABLE_PATTERN.matcher(processContent);
        while (matcher.find()) {
            String variable = matcher.group(2);
            if (isValidateVar(variable, "^[A-Z0-9_]+$")) {
                variavleSet.add(variable);
            }
        }
        return variavleSet;
    }

    /**
     * 判断给定的字符串是否符合对应的正则表达式
     *
     * @param varStr
     */
    public static boolean isValidateVar(String varStr, String pattern) {
        //String lowerNumRegex ="^[a-z0-9_]+$"; //alpha-numeric lowercase
        String upperNumRegex = pattern; //
        //return Pattern.compile(upperNumRegex).matcher(varStr).find() || Pattern.compile(lowerNumRegex).matcher(varStr).find();
        return Pattern.compile(upperNumRegex).matcher(varStr).find();
    }


    /**
     * https://stackoverflow.com/questions/5192512/how-can-i-clear-or-empty-a-stringbuilder
     * 1. setLength(0)   2. new one object   blew are difference about the two ways
     * There are basically two alternatives, using setLength(0) to reset the StringBuilder or creating a new one in each iteration. Both can have pros and cons depending on the usage.
     * If you know the expected capacity of the StringBuilder beforehand, creating a new one each time should be just as fast as setting a new length. It will also help the garbage collector, since each StringBuilder will be relatively short-lived and the gc is optimized for that.
     * When you don't know the capacity, reusing the same StringBuilder might be faster. Each time you exceed the capacity when appending, a new backing array has to be allocated and the previous content has to be copied. By reusing the same StringBuilder, it will reach the needed capacity after some iterations and there won't be any copying thereafter.
     * make stringBuilder object empty
     * @param stringBuilder
     */
    public static StringBuilder cleanStringBuilder(StringBuilder stringBuilder){
        stringBuilder.setLength(0);
        return stringBuilder;
    }

    /**
     * 从一个key=value形式的文件内容中组织成一个Map
     * (前提是key都别一样能完全保存)
     *
     * @param fileContent
     * @return
     */
    public static Map<String, String> getKeyValueMapFromFile(String fileContent) {
        Map<String, String> map = new HashMap<>();
        String[] lineArr = fileContent.split(System.getProperty("line.separator"));
        for (String line : lineArr) {
            if (!Strings.isNullOrEmpty(line)) {
                String[] split = line.split("=");
                map.put(split[0], split[1]);
            }
        }
        return map;
    }

    /**
     * 从一个文件内容中删除指定字符串开头的行
     * 应用: 去除脚本文件中的注释
     *
     * @param fileContent
     */
    private static String removeScriptComment(String fileContent, String symbol) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] lineArr = fileContent.split(System.getProperty("line.separator"));
        for (String line : lineArr) {
            if (!Strings.isNullOrEmpty(line)) {
                if (line.trim().startsWith(symbol)) {
                    continue;
                } else {
                    stringBuilder.append(line.trim()).append(System.getProperty("line.separator"));
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 删除List结合中没有用的元素(多用于处理脚本文件中通过split切割";"出来的list)
     * 去如下去除空, \n 和 \r 还可以再自定义
     *
     * @param list
     */
    public static void removeUselessStringInList(List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String ele = it.next();
            if (ele.equalsIgnoreCase("\n") || Strings.isNullOrEmpty(ele) || ele.equalsIgnoreCase("\r")) {
                it.remove();
            }
        }
    }

    /**
     * remove non-ASCII,ASCII control,non-printable characters
     * @param text
     * @return
     */
    public static String cleanTextContent(String text) {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");
        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");
        return text.trim();
    }

    /**
     * 将unicode码转化成字符串
     *
     * @param unicode
     * @return
     * @author shuai.ding
     */
    public static String unicode2String(String unicode) {
        if (org.apache.commons.lang3.StringUtils.isBlank(unicode)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        //如果pos位置后，有非中文字符，直接添加
        sb.append(unicode.substring(pos));

        return sb.toString();
    }

    /**
     * urlEncode a string
     * @param value
     * @return
     */
    // Method to encode a string value using `UTF-8` encoding scheme
    public static String urlEncodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    /**
     * 把一个集合中的元素,通过指定的分隔符连接起来
     *
     * @param collection
     * @param delimiter
     * @return
     */
    public static String flattenCollectionToString(Collection<?> collection, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        for (Object obj : collection) {
            buffer.append(obj.toString());
            buffer.append(delimiter);
        }

        if (buffer.length() > 0) {
            buffer.setLength(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * 处理文件格式内容,把Windows格式的文件输出成为Unix
     * Unix/Linux 都是\n  windows是\r\n  mac是\r(这里可以忽略mac)  把windows的\r全都替换成空
     *
     * @param text
     * @return
     */
    public static String handleStringFormat2Unix(String text) {
        return text.replace("\\r", "");
    }


    /**
     * 通过正则表达式的方式获取字符串中指定字符的个数
     *
     * @param text       指定的字符串
     * @param targetChar 对应的正则表达式
     * @return 指定字符的个数
     */
    public static int getTargetCharNum(String text, String targetChar) {
        // 根据指定的字符构建正则
        Pattern pattern = Pattern.compile(targetChar);
        // 构建字符串和正则的匹配
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        // 循环依次往下匹配
        while (matcher.find()) { // 如果匹配,则数量+1
            count++;
        }
        return count;
    }

    /**
     * 对字符串进行MD5加密
     *
     * @param md5
     * @return
     */
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    /**
     * count = 20, pagesize = 3 => 7pages
     * @param num
     * @param divisor
     * @return
     */
    public static int roundUp(String num, String divisor) {
        return (Integer.valueOf(num) + Integer.valueOf(divisor) - 1) / Integer.valueOf(divisor);
    }

    /**
     * 获取字符串中符合指定正则的部分
     * 比如获取http/hdfs的根目录
     *
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


    public static String strDiffChop(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return s1.substring(s2.length() - 1);
        } else if (s2.length() > s1.length()) {
            return s2.substring(s1.length() - 1);
        } else {
            return "";
        }
    }

    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == -1) {
            return EMPTY;
        }
        return str2.substring(at);
    }
    public static int indexOfDifference(String str1, String str2) {
        if (str1 == str2) {
            return -1;
        }
        if (str1 == null || str2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < str1.length() && i < str2.length(); ++i) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
        }
        if (i < str2.length() || i < str1.length()) {
            return i;
        }
        return -1;
    }

    @Test
    public void test() throws Exception {
        System.out.println(difference("AAAA", "AABABB"));
        char ctrlA = 48;
        System.out.println(getNonVisibleChar(ctrlA));
    }


}
