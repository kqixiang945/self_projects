package org.summerchill.cryption;

/**
 * 该类可以打成jar包 然后通过命令行交互,输入账号密码和盐值,生成加密之后的密码.
 * @author kxh
 * @description
 * @date 20210609_21:43
 */


import org.apache.commons.io.FileUtils;

import java.io.Console;
import java.io.File;
import java.util.Scanner;

public class MainClass {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String SALT = "salt";

    private static String username = "";
    private static String salt = "";
    private static String password = "";

    private static String makeSureAnswer = "";
    private static boolean ynFlag = false;

    private static Scanner scanner = new Scanner(System.in);
    private static Console console = System.console();

    private static PropertiesLoader loader = new PropertiesLoader("/application.properties");
    // 加密私钥
    private static String CRYPT_KEY = loader.getProperty("crypt_key");

    public static void main(String[] args) throws Exception {
        dealUserInput(USERNAME);
    }

    private static void dealUserInput(String type) {
        if (type.equalsIgnoreCase(USERNAME)) {//用户名
            ynFlag = getPass(USERNAME);
            if (ynFlag) {//用户名输入正确
                dealUserInput(PASSWORD);
            } else {//用户名输入错误
                dealUserInput(USERNAME);
            }
        } else if (type.equalsIgnoreCase(PASSWORD)) {//密码
            ynFlag = getPass(PASSWORD);
            if (ynFlag) {//密码输入正确
                dealUserInput(SALT);
            } else {//密码输入错误
                dealUserInput(PASSWORD);
            }
        } else if (type.equalsIgnoreCase(SALT)) {
            ynFlag = getPass(SALT);
            if (ynFlag) {//密码输入正确
                //System.out.println(String.format("用户名是:%s,密码是:%s,盐值是:%s 开始进行加密...", username, password,salt));
                //加密方法
                try {
                    //私钥 和 盐值进行DES加密 作为新的盐值
                    DESCryptHelper.initEncryptDecrypt(salt);
                    //私钥加密 生成新盐值
                    String aesSalt = DESCryptHelper.encrypt(CRYPT_KEY);
                    //对密码 和 新盐值加密 生成密文
                    String finalPassword = AESCryptHelper.encrypt(password, aesSalt);
                    StringBuilder sb = new StringBuilder();
                    sb.append("用户名:" + username + "\r\n");
                    sb.append("加密后的密码:" + finalPassword + "\r\n");
                    sb.append("盐值:" + salt + "\r\n");
                    sb.append("信息使用后请及时删除!!");
                    FileUtils.writeStringToFile(new File("加密后信息.txt"), sb.toString());
                    System.out.println("用户名: " + username + " 加密后密码: " + finalPassword + " 加密信息已经写入\"加密后信息.txt\" 文件中,请检查");
                    //System.out.println("解密后的密码: " + AESCryptHelper.decrypt(finalPassword,aesSalt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {//密码输入错误
                dealUserInput(SALT);
            }
        }
    }

    private static boolean getPass(String type) {
        if (type.equalsIgnoreCase(USERNAME)) {
            //对应用户名的输入的逻辑处理
            System.out.print("请输入数据库用户名:");
            username = scanner.next();
            System.out.println("您输入的数据库用户名是:" + username + " 请按y/n后回车确认!");
            makeSureAnswer = scanner.next();
            ynFlag = makeSureAnswer.equalsIgnoreCase("Y") || makeSureAnswer.equalsIgnoreCase("YES") ? true : false;
            return ynFlag;
        } else if (type.equalsIgnoreCase(PASSWORD)) {
            if (console != null) {
                //对于密码的输入的逻辑处理 这里使用控制台下密码的输入的方式 不会明文显示密码!
                password = new String(console.readPassword("请输入数据库密码,输入后请回车:"));
                /*  这个是普通的控制台输入 会明文显示密码!!
                System.out.print("请输入数据库密码,输入后请回车:");
                password = scanner.next();
                System.out.print("请再次输入一遍数据库密码,输入后请回车:");
                String password_again = scanner.next();
                */
                String password_again = new String(console.readPassword("请再次输入一遍数据库密码,输入后请回车:"));
                if (password.equals(password_again)) {
                    ynFlag = true;
                } else {
                    System.out.println("两次输入的密码不一致,请你重新再次输入!");
                    ynFlag = false;
                }
                return ynFlag;
            }
        } else if (type.equalsIgnoreCase(SALT)) {
            //对于盐值的输入的业务处理
            System.out.println("请输入一个随机字符串,作为对用户名,密码加密的盐值:");
            salt = scanner.next();
            System.out.println("您再次输入一遍加密盐值,输入后请回车:");
            String salt_again = scanner.next();
            if (salt.equals(salt_again)) {
                ynFlag = true;
            } else {
                System.out.println("两次输入的盐值不一致,请你重新再次输入!");
                ynFlag = false;
            }
            return ynFlag;
        } else {
            System.out.println("非法情况!");
            System.exit(-1);
        }
        return ynFlag;
    }

    /*@Test
    public void encrypt() throws Exception {
        String password_encrypt = "A400A098EC8B8A688459B64A1C9C259A";
        String salt = "8992";
        //私钥 和 盐值进行DES加密 作为新的盐值
        DESCryptHelper.initEncryptDecrypt(salt);
        //私钥加密 生成新盐值
        String aesSalt = DESCryptHelper.encrypt(CRYPT_KEY);
        String decrypt = AESCryptHelper.decrypt(password_encrypt, aesSalt);
        System.out.println("解密后的密码: " + AESCryptHelper.decrypt(password_encrypt, aesSalt));
    }*/

}
