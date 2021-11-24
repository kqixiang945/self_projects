package com.summerchill.string;

import java.util.Arrays;

public class StringUtils {

    /**
     * 在指定的位置后面添加字符串,字符串从0开始.
     *
     * @param originalString
     * @param stringToBeInserted
     * @param index
     * @return
     */
    public static String insertString(String originalString, String stringToBeInserted, int index) {
        // Create a new string
        String newString = originalString.substring(0, index + 1) + stringToBeInserted + originalString.substring(index + 1);
        // return the modified String
        return newString;
    }


    /**
     * 判断一个字符串中 是否包含一个字符串数组中的元素.
     *
     * @param inputStr
     * @param items
     * @return
     */
    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }

    /**
     * 把字符串中的多个连续的空格替换成一个
     * 可参考: https://stackoverflow.com/questions/2932392/java-how-to-replace-2-or-more-spaces-with-single-space-in-string-and-delete-lead
     *
     * @param string
     * @return
     */
    public static String multipleWhiteSpaceReplaceOne(String string) {
        return string.trim().replaceAll(" +", " ");
    }

}
