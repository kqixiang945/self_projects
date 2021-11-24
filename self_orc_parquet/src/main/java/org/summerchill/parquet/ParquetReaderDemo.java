package org.summerchill.parquet;

/**
 * @author kxh
 * @description
 * @date 20210629_16:40
 */
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.schema.GroupType;

public class ParquetReaderDemo {

    public static String FILE_PATH = "/Users/kongxiaohan/Desktop/000000_0_p";

    public static void main(String[] args) throws Exception {
        GroupReadSupport groupReadSupport = new GroupReadSupport();
        ParquetReader<Group> parquetReader = ParquetReader.builder(groupReadSupport, new Path(FILE_PATH)).build();
        Group group;
        while((group = parquetReader.read()) != null) {
            GroupType groupType = group.getType();
            groupType.getFields().forEach(System.out::println);
            break;
        }

    }

}