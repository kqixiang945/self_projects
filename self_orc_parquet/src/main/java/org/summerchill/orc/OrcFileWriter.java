package org.summerchill.orc;

/**
 * @author kxh
 * @description
 * @date 20210628_17:48
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.common.type.HiveDecimal;
import org.apache.hadoop.hive.ql.exec.vector.*;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.BiConsumer;

public class OrcFileWriter {
    public static void main(String[] args) throws IOException {
        List<Map<String, Object>> data = new LinkedList<>();
        //Map.of
        //data.add(Map.of("systemno_alm", "dfadfa", "systemname_alm", "Laptop", "systemno", "43434",
        //"systemname","3232323","business_alm","3313212","job_funciton_alm","45343","center_alm","23饿323","system_source","12121"));

        String struct = "struct<systemno_alm:string,systemname_alm:string,systemno:string,systemname:string," +
                "business_alm:string,job_funciton_alm:string,center_alm:string,system_source:string>";
        write(new Configuration(), "orders.orc2", struct, data);

        System.out.println("Done");
    }

    /**
     * Type specific ColumnVectors are required to write column values to the file.
     * Since ORC files are column-oriented, data is stored using column vectors and read using column vectors. The ORC writer should know the schema of the data to cast the ColumnVector and write data according to the type.
     *
     * Apart from the technical details, running this class will create a new ORC file named orders.orc with three rows.
     * @param configuration
     * @param path
     * @param struct
     * @param data
     * @throws IOException
     */
    public static void write(Configuration configuration, String path, String struct, List<Map<String, Object>> data) throws IOException {
        // Create the schemas and extract metadata from the schema
        TypeDescription schema = TypeDescription.fromString(struct);
        List<String> fieldNames = schema.getFieldNames();
        List<TypeDescription> columnTypes = schema.getChildren();

        // Create a row batch
        VectorizedRowBatch batch = schema.createRowBatch();

        // Get the column vector references
        List<BiConsumer<Integer, Object>> consumers = new ArrayList<>(columnTypes.size());
        for (int i = 0; i < columnTypes.size(); i++) {
            TypeDescription type = columnTypes.get(i);
            ColumnVector vector = batch.cols[i];
            consumers.add(createColumnWriter(type, vector));
        }

        // Open a writer to write the data to an ORC fle
        try (Writer writer = OrcFile.createWriter(new Path(path),
                OrcFile.writerOptions(configuration)
                        .setSchema(schema))) {
            for (Map<String, Object> row : data) {
                // batch.size should be increased externally
                int rowNum = batch.size++;

                // Write each column to the associated column vector
                for (int i = 0; i < fieldNames.size(); i++) {
                    consumers.get(i).accept(rowNum, row.get(fieldNames.get(i)));
                }

                // If the buffer is full, write it to disk
                if (batch.size == batch.getMaxSize()) {
                    writer.addRowBatch(batch);
                    batch.reset();
                }
            }

            // Check unwritten rows before closing
            if (batch.size != 0) {
                writer.addRowBatch(batch);
            }
        }
    }

    /**
     * The OrcFileWriter can be made generic using a utility method to cast the ColumnVector based on the type and to put the data in the vector.
     * Create a new static method named createColumnWriter in the same class with the following code:
     * @param description
     * @param columnVector
     * @return
     */
    public static BiConsumer<Integer, Object> createColumnWriter(TypeDescription description, ColumnVector columnVector) {
        String type = description.getCategory().getName();
        BiConsumer<Integer, Object> consumer;
        if ("tinyint".equals(type)) {
            consumer = (row, val) -> ((LongColumnVector) columnVector).vector[row] = ((Number) val).longValue();
        } else if ("smallint".equals(type)) {
            consumer = (row, val) -> ((LongColumnVector) columnVector).vector[row] = ((Number) val).longValue();
        } else if ("int".equals(type) || "date".equals(type)) {
            // Date is represented as int epoch days
            consumer = (row, val) -> ((LongColumnVector) columnVector).vector[row] = ((Number) val).longValue();
        } else if ("bigint".equals(type)) {
            consumer = (row, val) -> ((LongColumnVector) columnVector).vector[row] = ((Number) val).longValue();
        } else if ("boolean".equals(type)) {
            consumer = (row, val) -> ((LongColumnVector) columnVector).vector[row] = (Boolean) val ? 1 : 0;
        } else if ("float".equals(type)) {
            consumer = (row, val) -> ((DoubleColumnVector) columnVector).vector[row] = ((Number) val).floatValue();
        } else if ("double".equals(type)) {
            consumer = (row, val) -> ((DoubleColumnVector) columnVector).vector[row] = ((Number) val).doubleValue();
        } else if ("decimal".equals(type)) {
            consumer = (row, val) -> ((DecimalColumnVector) columnVector).vector[row].set(HiveDecimal.create((BigDecimal) val));
        } else if ("string".equals(type) || type.startsWith("varchar") || "char".equals(type)) {
            consumer = (row, val) -> {
                byte[] buffer = val.toString().getBytes(StandardCharsets.UTF_8);
                ((BytesColumnVector) columnVector).setRef(row, buffer, 0, buffer.length);
            };
        } else if ("timestamp".equals(type)) {
            consumer = (row, val) -> ((TimestampColumnVector) columnVector).set(row, (Timestamp) val);
        } else {
            throw new RuntimeException("Unsupported type " + type);
        }
        return consumer;
    }
}