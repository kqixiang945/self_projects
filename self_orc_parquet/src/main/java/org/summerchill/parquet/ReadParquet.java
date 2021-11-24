package org.summerchill.parquet;

/**
 * @author kxh
 * @description
 * @date 20210629_17:24
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetReader.Builder;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class ReadParquet {
    static Logger logger = Logger.getLogger(ReadParquet.class);

    public static void main(String[] args) throws Exception {

        //parquetWriter("test\\parquet-out8","/Users/kongxiaohan/Desktop/input_p");
        parquetReaderV2("/Users/kongxiaohan/Desktop/000000_0_p");
        parquetReaderV2("test\\parquet-out8");
    }


    static void parquetReaderV2(String inPath) throws Exception {
        GroupReadSupport readSupport = new GroupReadSupport();
        Builder<Group> reader = ParquetReader.builder(readSupport, new Path(inPath));
        ParquetReader<Group> build = reader.build();
        Group line = null;
        while ((line = build.read()) != null) {
            //Group time = line.getGroup("time", 0);
            //通过下标和字段名称都可以获取
/*System.out.println(line.getString(0, 0)+"\t"+
line.getString(1, 0)+"\t"+
time.getInteger(0, 0)+"\t"+
time.getString(1, 0)+"\t");*/

            System.out.println(line.getString("systemno_alm", 0) + "\t" +
                    line.getString("systemname_alm", 0) + "\t" +
                    line.getString("systemno", 0) + "\t" +
                    line.getString("systemname", 0) + "\t" +
                    line.getString("business_alm", 0) + "\t" +
                    line.getString("job_funciton_alm", 0) + "\t" +
                    line.getString("center_alm", 0) + "\t" +
                    line.getString("system_source", 0) + "\t" +
                    line.getString("extend_1", 0) + "\t" +
                    line.getString("extend_2", 0) + "\t" +
                    line.getString("extend_3", 0) + "\t" +
                    line.getString("extend_4", 0) + "\t" +
                    line.getString("extend_5", 0) + "\t" +
                    line.getString("op_time", 0) + "\t" +
                    line.getString("execution_id", 0) + "\t" +
                    line.getString("load_date", 0) + "\t"
            );

            //System.out.println(line.toString());

        }
        System.out.println("读取结束");
    }

    //新版本中new ParquetReader()所有构造方法好像都弃用了,用上面的builder去构造对象
    static void parquetReader(String inPath) throws Exception {
        GroupReadSupport readSupport = new GroupReadSupport();
        ParquetReader<Group> reader = new ParquetReader<Group>(new Path(inPath), readSupport);
        Group line = null;
        while ((line = reader.read()) != null) {
            System.out.println(line.toString());
        }
        System.out.println("读取结束");

    }

    /**
     * @param outPath 输出Parquet格式
     * @param inPath  输入普通文本文件
     * @throws IOException
     */
    static void parquetWriter(String outPath, String inPath) throws IOException {
        MessageType schema = MessageTypeParser.parseMessageType("message d02_bpm_alm_system_mapping {\n" +
                " required binary systemno_alm (UTF8);\n" +
                " required binary systemname_alm (UTF8);\n" +
                " required binary systemno (UTF8);\n" +
                " required binary systemname (UTF8);\n" +
                " required binary business_alm (UTF8);\n" +
                " required binary job_funciton_alm (UTF8);\n" +
                " required binary center_alm (UTF8);\n" +
                " required binary system_source (UTF8);\n" +
                " required binary extend_1 (UTF8);\n" +
                " required binary extend_2 (UTF8);\n" +
                " required binary extend_3 (UTF8);\n" +
                " required binary extend_4 (UTF8);\n" +
                " required binary extend_5 (UTF8);\n" +
                " required binary op_time (UTF8);\n" +
                " required binary execution_id (UTF8);\n" +
                " required binary load_date (UTF8);\n" +
                "}");
        GroupFactory factory = new SimpleGroupFactory(schema);
        Path path = new Path(outPath);
        Configuration configuration = new Configuration();
        GroupWriteSupport writeSupport = new GroupWriteSupport();
        writeSupport.setSchema(schema, configuration);
        //ParquetWriter<Group> writer = new ParquetWriter<Group>(path, configuration, writeSupport);
        ParquetWriter<Group> writer = new ParquetWriter<>(path, configuration, writeSupport);
        //把本地文件读取进去，用来生成parquet格式文件
        BufferedReader br = new BufferedReader(new FileReader(new File(inPath)));
        String line = "";
        Random r = new Random();
        for (int i = 0; i < 500; i++) {
            Group group = factory.newGroup()
                    .append("systemno_alm", "systemno_alm" + i)
                    .append("systemname_alm", "systemname_alm" + i)
                    .append("systemno", "systemno" + i)
                    .append("systemname", "systemname" + i)
                    .append("business_alm", "business_alm" + i)
                    .append("job_funciton_alm", "job_funciton_alm" + i)
                    .append("center_alm", "center_alm" + i)
                    .append("system_source", "system_source" + i)
                    .append("extend_1", "extend_1" + i)
                    .append("extend_2", "extend_2" + i)
                    .append("extend_3", "extend_3" + i)
                    .append("extend_4", "extend_4" + i)
                    .append("extend_5", "extend_5" + i)
                    .append("op_time", "op_time" + i)
                    .append("execution_id", "execution_id" + i)
                    .append("load_date", "load_date" + i);
            writer.write(group);
        }


        //while ((line = br.readLine()) != null) {
        //    String[] strs = line.split("\\s+");
        //    if (strs.length == 2) {
        //        Group group = factory.newGroup()
        //                .append("city", strs[0])
        //                .append("ip", strs[1]);
        //        //Group tmpG = group.addGroup("time");
        //        //tmpG.append("ttl", r.nextInt(9) + 1);
        //        //tmpG.append("ttl2", r.nextInt(9) + "_a");
        //        writer.write(group);
        //    }
        //}

        System.out.println("write end");
        writer.close();
    }
}