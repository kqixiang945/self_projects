package org.summerchill.parquet;

/**
 * @author kxh
 * @description
 * @date 20210629_17:06
 */
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.example.data.simple.SimpleGroup;
import java.io.IOException;
import org.apache.parquet.schema.Type;
import java.util.List;
import java.util.Map;


public class ParquetReader{
    public static void main(String args[]) throws IOException{
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("User's current working directory is: "+currentDirectory);

        Parquet parquet = null;
        //ParquetReaderUtils.getParquetData(currentDirectory+"/src/main/resources/part-00000.parquet");
        List<Type> schemaFieldList = parquet.getSchemaFields();
        List<SimpleGroup> datagroup = parquet.getData();
        MessageType schema = parquet.getSchema();
        Map<String, String> keyValueFileMetaData = parquet.getKeyValueMetadata();

        System.out.println("\nData ---->");
        for (SimpleGroup data : datagroup){
            System.out.println(data.toString());
        }

        System.out.println("\nSchema ---->");
        for (Type schemaField: schemaFieldList){
            System.out.println("Name: "+schemaField.getName());
            System.out.println("Original Type: "+schemaField.getOriginalType());
            System.out.println("Primitive Type: "+schemaField.asPrimitiveType());
        }

        System.out.println("\n Schema toString() ------> "+ schema.toString());

        System.out.println("\nFile metadata key values ---->");
        for (Map.Entry<String, String> entry : keyValueFileMetaData.entrySet()) {
            System.out.println("Key - "+ entry.getKey());
            System.out.println("Value - " + entry.getValue());
        }
    }
}