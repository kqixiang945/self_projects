package org.summerchill;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 个人总结的集合相关的工具类
 */
public class CollectionUtils {

    public static void main(String[] args) {
        //1.把一个数组转换成List
        List<String> arry2List = convertArrayToList(new String[]{"aaa", "bbb"});

        //2.数组中的元素用第一个参数包裹,每个元素用为第二个参数分隔 形成一个字符串. 如下形成'aaa','bbb'的字符串.
        String arrayWithContactSymbol = arrayConevertStringWithSeparator("'", ",", new String[]{"aaa", "bbb"});

        //3.在HiveTable对象组成的list中找到是否有指定名字的,如果有多个只返回一个.
        HiveTable hiveTable = findHiveTableSpecifyTblNameValueFormList(new ArrayList<HiveTable>(), "tableAAA");

        //4.判断一个数组中是不是含有给定的元素
        String[] transformType = {"wgs2gaode", "gaode2wgs", "gaode2baidu", "baidu2gaode", "wgs2baidu", "baidu2wgs"};
        judgeArrayContainSpecifyElement(transformType, "gaode2baidu");
        judgeArrayContainSpecifyElement2(transformType, "gaode2baidu");
        int[] intArray = {1, 2, 3, 4};
        judgeArrayContainSpecifyElement3(intArray, 4);

        //5.打印集合中的元素


    }

    /**
     * 打印一个集合中的所有元素
     */
    public static void printCollectionElement(){
        Set<String> tempSet = new HashSet();
        //一行一个元素
        tempSet.stream().forEach(s -> System.out.println(s));
        //中间用逗号隔开



    }



    /**
     * 判断字符串数组中是不是有指定的字符串
     *
     * @param array
     * @param specifyStr
     * @return
     */
    public static boolean judgeArrayContainSpecifyElement(String[] array, String specifyStr) {
        return Arrays.asList(array).contains(specifyStr);
    }

    /**
     * 判断字符串数组中是不是有指定的字符串
     *
     * @param array
     * @param specifyStr
     * @return
     */
    public static boolean judgeArrayContainSpecifyElement2(String[] array, String specifyStr) {
        boolean containFlag = Arrays.stream(array).anyMatch(specifyStr::equals);
        return containFlag;
    }

    /**
     * 判断int数组中是不是有指定的数字
     *
     * @param array
     * @param specifyEle
     * @return
     */
    public static boolean judgeArrayContainSpecifyElement3(int[] array, int specifyEle) {
        boolean containFlag = IntStream.of(array).anyMatch(x -> x == specifyEle);
        return containFlag;
    }

    /**
     * 快速判断一个含有某个属性的元素 是否存在一个List中的对象中.
     * 如下方法是在一个List(封装的hive表对象) 找是否有指定名字的表,并返回.
     *
     * @param hiveTableList
     * @param tableName
     * @return
     */
    public static HiveTable findHiveTableSpecifyTblNameValueFormList(final List<HiveTable> hiveTableList, final String tableName) {
        try {
            return hiveTableList.stream().filter(o -> o.getHiveTableName().equalsIgnoreCase(tableName)).findFirst().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class HiveTable {
        //hive表id  这个字段在对应的表中没有...表中的数据来源可能不仅仅是hive中的.
        private String hiveTableId;
        //hive表所在库名称
        private String hiveDbName;
        //hive表名称
        private String hiveTableName;
        //connection  hivemetastore中的全部都是"hive"  表中对应的可能有多种来源.
        private String hiveConnection;
        //hive表的分区名称(可能是多个,用逗号隔开)
        private String partitionName;

        public String getHiveTableName() {
            return hiveTableId;
        }

        public void setHiveTableName(String hiveDbName) {
            this.hiveDbName = hiveDbName;
        }
    }


    /**
     * @return
     */
    public static String arrayConevertStringWithSeparator(String quote, String separator, String[] strArray) {
        //把一个数组转换成单引号引用,逗号分隔的字符串.
        String str = "";
        str = String.join(separator, Arrays.asList(strArray).stream().map(name -> (quote + name + quote)).collect(Collectors.toList()));
        return str;
    }


    /**
     * Generic function to convert an Array to List
     * 通用的任意类型的数组转换成对应类型的List对象
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> List<T> convertArrayToList(T array[]) {
        // Create an empty List
        List<T> list = new ArrayList<>();
        // Iterate through the array
        for (T t : array) {
            // Add each element into the list
            list.add(t);
        }
        // Return the converted List
        return list;
    }


}
