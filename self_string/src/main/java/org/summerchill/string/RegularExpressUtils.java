package org.summerchill.string;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kxh
 * @description 正则表达式相关的工具类
 * @date 20210620_12:13
 */
public class RegularExpressUtils {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("(\\$)\\{?(\\w+)\\}?");

    public static void main(String[] args) {
        //1.获取字符串中的变量(${***} 或者 $*** 两种类型的)
        getVariables("字符串");

        //2.正则表达式判断 是"下划线和数字的组合"
        judgeNumberUnderScore("");


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
            if (isValidateVar(variable,"^[A-Z0-9_]+$")) {
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
     * 正则表达式:
     * {n,} n 是一个非负整数。至少匹配n 次
     * "{" 标记限定符表达式的开始。
     * "[" 标记一个中括号表达式的开始。要匹配 [，请使用 \[。
     * "^" 匹配输入字符串的开始位置，除非在方括号表达式中使用，当该符号在方括号表达式中使用时，表示不接受该方括号表达式中的字符集合。要匹配 ^ 字符本身，请使用 \^。
     * <p>
     * 如下正则表达式判断 是"下划线和数字的组合"
     * <p>
     * "23_23w" 这种false
     * "23_23"  这种true
     *
     * @param string
     * @return
     */
    public static boolean judgeNumberUnderScore(String string) {
        Pattern pattern = Pattern.compile("^[0-9_]{1,}$");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

}
