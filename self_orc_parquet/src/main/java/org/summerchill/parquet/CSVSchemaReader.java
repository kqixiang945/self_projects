package org.summerchill.parquet;

/**
 * @author kxh
 * @description
 * @date 20210629_17:06
 */
import org.apache.parquet.schema.MessageType;
import java.io.IOException;

public class CSVSchemaReader {
    public static void main(String args[]) throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("User's current working directory is: " + currentDirectory);

        MessageType csvSchema = CSVSchemaReaderUtils.csvSchema(currentDirectory + "/src/main/resources/testschema.csv");
        System.out.println(csvSchema.toString());
    }
}