package org.summerchill.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.OriginalType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Types;

import java.io.IOException;
import java.util.Random;

/**
 * @author kxh
 * @description
 * @date 20210705_17:17
 */
public class ParquestTest {
    public static void main(String[] args) {
        writeParquet("ttttt");
    }

    public static MessageType getMessageTypeFromCode(){
        MessageType messageType =
                Types.buildMessage()
                        .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("id")
                        .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("name")
                        .required(PrimitiveType.PrimitiveTypeName.INT32).named("age")
                        .requiredGroup()
                        .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("test1")
                        .required(PrimitiveType.PrimitiveTypeName.BINARY).as(OriginalType.UTF8).named("test2")
                        .named("group1")
                        .named("trigger");
        return messageType;
    }

    public static void writeParquet(String name){

        // 1. 声明parquet的messageType
        MessageType messageType = getMessageTypeFromCode();
        System.out.println(messageType.toString());

        // 2. 声明parquetWriter
        Path path = new Path("/tmp/etl/"+ name);
        Configuration configuration = new Configuration();
        GroupWriteSupport.setSchema(messageType, configuration);
        GroupWriteSupport writeSupport = new GroupWriteSupport();

        // 3. 写数据
        ParquetWriter<Group> writer = null;
        try {
            writer = new ParquetWriter<Group>(path,
                    ParquetFileWriter.Mode.CREATE,
                    writeSupport,
                    CompressionCodecName.UNCOMPRESSED,
                    128*1024*1024,
                    5*1024*1024,
                    5*1024*1024,
                    ParquetWriter.DEFAULT_IS_DICTIONARY_ENABLED,
                    ParquetWriter.DEFAULT_IS_VALIDATING_ENABLED,
                    ParquetWriter.DEFAULT_WRITER_VERSION,
                    configuration);
            Random random = new Random();

            for(int i=0; i<10; i++){
                // 4. 构建parquet数据，封装成group
                Group group = new SimpleGroupFactory(messageType).newGroup();
                group.append("name", i+"@qq.com")
                        .append("id",i+"@id")
                        .append("age",i)
                        .addGroup("group1")
                        .append("test1", "test1"+i)
                        .append("test2","test2"+i);
                writer.write(group);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static void readParquet(String name){
    //    // 1. 声明readSupport
    //    GroupReadSupport groupReadSupport = new GroupReadSupport();
    //    Path path = new Path("/tmp/etl/"+name);
    //
    //    // 2.通过parquetReader读文件
    //   ParquetReader<Group> reader = null;
    //    try {
    //        reader = ParquetReader.builder(groupReadSupport, path).build();
    //        Group group = null;
    //        while ((group = reader.read()) != null){
    //            System.out.println(group);
    //        }
    //
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    } finally {
    //        if(reader != null){
    //            try {
    //                reader.close();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    }

}
