package org.summerchill.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.format.converter.ParquetMetadataConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.metadata.ParquetMetadata;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.MessageColumnIO;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;

import java.io.IOException;

/**
 * @author kxh
 * @description 读取Parquet文件的工具类
 * @date 20211125_12:49
 */
public class ParquetFileUtils {

    private static Path path = new Path("file:///Users/kongxiaohan/Desktop/temp/datekey=2021-04/000002_0");

    private static void printGroup(Group g) {
        int fieldCount = g.getType().getFieldCount();
        for (int field = 0; field < fieldCount; field++) {
            int valueCount = g.getFieldRepetitionCount(field);

            Type fieldType = g.getType().getType(field);
            String fieldName = fieldType.getName();

            for (int index = 0; index < valueCount; index++) {
                if (fieldType.isPrimitive()) {
                    System.out.println(fieldName + " " + g.getValueToString(field, index));
                }
            }
        }
        System.out.println("");
    }

    public static void main(String[] args) throws IllegalArgumentException {

        Configuration conf = new Configuration();

        try {
            ParquetMetadata readFooter = ParquetFileReader.readFooter(conf, path, ParquetMetadataConverter.NO_FILTER);
            MessageType schema = readFooter.getFileMetaData().getSchema();
            ParquetFileReader r = new ParquetFileReader(conf, path, readFooter);

            PageReadStore pages = null;
            try {
                while (null != (pages = r.readNextRowGroup())) {
                    final long rows = pages.getRowCount();
                    System.out.println("Number of rows: " + rows);

                    final MessageColumnIO columnIO = new ColumnIOFactory().getColumnIO(schema);
                    final RecordReader recordReader = columnIO.getRecordReader(pages, new GroupRecordConverter(schema));
                    for (int i = 0; i < rows; i++) {
                        System.out.println("打印第" + (i + 1) + "行数据....");
                        final Group g = (Group) recordReader.read();
                        printGroup(g);
                        // TODO Compare to System.out.println(g);
                    }
                }
            } finally {
                r.close();
            }
        } catch (IOException e) {
            System.out.println("Error reading parquet file.");
            e.printStackTrace();
        }
    }

    //public static String readParquetFile() throws IOException {
    //    //Path file = new Path("/Users/kongxiaohan/Desktop/000000_0");
    //    File file = new File("/Users/kongxiaohan/Desktop/000000_0");
    //
    //    ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(file).build();
    //    GenericRecord nextRecord = reader.read();
    //
    //    return "";
    //}


}
