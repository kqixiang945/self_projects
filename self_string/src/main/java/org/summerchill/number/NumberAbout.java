package org.summerchill.number;

/**
 * @author kxh
 * @description 数字相关的转换类
 * @date 20210820_20:31
 */
public class NumberAbout {
    /**
     * 2进制 前置0b
     * 8进制： 前置 0
     * 10进制： 不需前置
     * 16进制： 前置 0x 或者 0X
     * @param args
     */
    public static void main(String[] args) {

        int octalB = 012;
        int hexB = 0x12;
        System.out.println(octalB);
        System.out.println(hexB);

        //8进制、10进制、16进制转为2进制
        System.out.println("Integer.toBinaryString(01)="+Integer.toBinaryString(01));
        System.out.println("Integer.toBinaryString(012)="+Integer.toBinaryString(012));
        System.out.println("Integer.toBinaryString(10)="+Integer.toBinaryString(10));
        System.out.println("Integer.toBinaryString(0xa)="+Integer.toBinaryString(0xa));

        System.out.println("Integer.toOctalString(0x12)="+Integer.toOctalString(0x12));
        System.out.println("Integer.toOctalString(18)="+Integer.toOctalString(18));

        System.out.println("Integer.toHexString(012)="+Integer.toHexString(012));
        System.out.println("Integer.toHexString(10)="+Integer.toHexString(10));
    }
}
