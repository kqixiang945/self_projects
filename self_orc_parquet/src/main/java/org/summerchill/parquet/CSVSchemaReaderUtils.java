package org.summerchill.parquet;

/**
 * @author kxh
 * @description
 * @date 20210629_17:08
 */
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class CSVSchemaReaderUtils {
    private static final BufferedReader readCSVFile(String filepath) throws IOException{
        return new BufferedReader(new FileReader(filepath));
    }

    private static List<Type> readSchemaCSV(String filePath){
        List<Type> schemaFields = new ArrayList<>();
        try {
            BufferedReader br = readCSVFile(filePath);
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] fields = line.split(",");
                String name = fields[0];
                String type = fields[1];
                CSVSchemaField csvSchemaField = new CSVSchemaField(name, type);
                schemaFields.add(csvSchemaField.getPrimitiveType());
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return schemaFields;
    }

    public static MessageType csvSchema(String filePath){
        return new MessageType("root", readSchemaCSV(filePath));
    }
}