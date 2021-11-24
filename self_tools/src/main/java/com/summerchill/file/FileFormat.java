package com.summerchill.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * 判断一个文件的格式是windows 还是unix 还是mac
 * 对应参考地址:
 * https://stackoverflow.com/questions/3066511/how-to-determine-file-format-dos-unix-mac
 * Unix 系统里，每行结尾只有“<换行>”，即“\n”；
 * Windows系统里面，每行结尾是“<回车><换行>”，即“\r\n”；
 * Mac系统里，每行结尾是“<回车>”，即“\r”。
 */
public class FileFormat {
    private static final char CR = '\r';
    private static final char LF = '\n';

    public static FileType discover(String fileName) throws IOException {

        Reader reader = new BufferedReader(new FileReader(fileName));
        FileType result = discover(reader);
        reader.close();
        return result;
    }

    private static FileType discover(Reader reader) throws IOException {
        int c;
        while ((c = reader.read()) != -1) {
            switch (c) {
                case LF:
                    return FileType.UNIX;
                case CR: {
                    if (reader.read() == LF) {
                        return FileType.WINDOWS;
                    }
                    return FileType.MAC;
                }
                default:
                    continue;
            }
        }
        return FileType.UNKNOWN;
    }

    public static void main(String[] args) throws IOException {
        String filepath = "/Users/kongxiaohan/project_code/idea/summerchill/bi_task/hive/app/hv_e2_app_a22_gxc_device_value_temp.conf";
        FileType discover = discover(filepath);
        //FileType是枚举类型,对应枚举值要使用toString()方法住转换
        System.out.println(discover.toString());
    }

    public enum FileType {WINDOWS, UNIX, MAC, UNKNOWN}
}
