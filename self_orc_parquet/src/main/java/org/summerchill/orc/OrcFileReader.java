package org.summerchill.orc;

/**
 * @author kxh
 * @description
 * @date 20210628_17:49
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.*;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;
import org.apache.orc.TypeDescription;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class OrcFileReader {
    private static final int BATCH_SIZE = 2048;

    public static void main(String[] args) throws IOException {
        //List<Map<String, Object>> rows = read(new Configuration(), "000000_0_o");
        List<Map<String, Object>> rows = read(new Configuration(), "orders.orc2");
        for (Map<String, Object> row : rows) {
            System.out.println(row);
        }
    }

    public static List<Map<String, Object>> read(Configuration configuration, String path) throws IOException {
        //configuration.set("fs.defaultFS", "hdfs://10.9.62.200:8020");
        configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        configuration.setBoolean("fs.hdfs.impl.disable.cache", true);
        System.setProperty("HADOOP_USER_NAME", "etluser");

        // Create a list to collect rows
        List<Map<String, Object>> rows = new LinkedList<>();

        // Create an ORC reader using the Hadoop fileSystem and path
        try (Reader reader = OrcFile.createReader(new Path(path), OrcFile.readerOptions(configuration))) {
            // Extract the schema and metadata from the reader
            TypeDescription schema = reader.getSchema();
            List<String> fieldNames = schema.getFieldNames();
            List<TypeDescription> columnTypes = schema.getChildren();

            // Select only order_id and price
            List<Integer> selectedColumns = new ArrayList<>();
            //set.add("systemno_alm");
            //set.add("systemname_alm");
            //set.add("systemno");
            //set.add("systemname");
            //set.add("business_alm");
            //set.add("job_funciton_alm");
            //set.add("center_alm");
            //set.add("system_source");
            //set.add("extend_1");
            //set.add("extend_2");
            //set.add("extend_3");
            //set.add("extend_4");
            //set.add("extend_5");
            //set.add("op_time");
            //set.add("execution_id");
            //set.add("load_date");

            String struct = "systemno_alm,systemname_alm,systemno,systemname,business_alm,job_funciton_alm,center_alm,system_source";
            boolean[] columnsToRead = createColumnsToRead(schema,Set.of(struct), selectedColumns);

            // Get the column vector references
            int size = fieldNames.size();
            BiFunction<ColumnVector, Integer, Object>[] mappers = new BiFunction[size];
            for (int i : selectedColumns) {
                TypeDescription type = columnTypes.get(i);
                mappers[i] = createColumnReader(type);
            }

            // Pass the columnsToRead to the reader to read only the selected columns
            try (RecordReader records = reader.rows(reader.options().include(columnsToRead))) {
                // Read rows in batch for better performance.
                VectorizedRowBatch batch = reader.getSchema().createRowBatch(BATCH_SIZE);
                while (records.nextBatch(batch)) {
                    for (int row = 0; row < batch.size; row++) {
                        // Read rows from the batch
                        Map<String, Object> map = new HashMap<>(selectedColumns.size());
                        for (int col : selectedColumns) {
                            ColumnVector columnVector = batch.cols[col];
                            if (columnVector.isNull[row]) {
                                map.put(fieldNames.get(col), null);
                            } else {
                                Object value = mappers[col].apply(columnVector, row);
                                map.put(fieldNames.get(col), value);
                            }
                        }
                        rows.add(map);
                    }
                }
            }
        }
        return rows;
    }

    /**
     * The ORC Reader#rows method takes a Reader.Options object. Using this object, we can control the columns to read.
     * A boolean array is used to mark the columns that we want to read from an ORC file.
     *
     * Create a new static method as shown below.
     * This method constructs the boolean array to select columns and add the selected colum indices to a list for future references.
     * @param schema
     * @param columns
     * @param indices
     * @return
     */
    public static boolean[] createColumnsToRead(TypeDescription schema, Set<String> columns, List<Integer> indices) {
        // Create an array of boolean
        boolean[] columnsToRead = new boolean[schema.getMaximumId() + 1];
        List<String> fieldNames = schema.getFieldNames();
        List<TypeDescription> columnTypes = schema.getChildren();
        for (int i = 0; i < fieldNames.size(); i++) {
            if (columns.contains(fieldNames.get(i))) {
                indices.add(i);
                TypeDescription type = columnTypes.get(i);
                for (int id = type.getId(); id <= type.getMaximumId(); id++) {
                    columnsToRead[id] = true;
                }
            }
        }
        return columnsToRead;
    }

    public static BiFunction<ColumnVector, Integer, Object> createColumnReader(TypeDescription description) {
        // Reference: https://orc.apache.org/docs/core-java.html
        String type = description.getCategory().getName();
        BiFunction<ColumnVector, Integer, Object> mapper;
        if ("tinyint".equals(type)) {
            mapper = (columnVector, row) -> (byte) ((LongColumnVector) columnVector).vector[row];
        } else if ("smallint".equals(type)) {
            mapper = (columnVector, row) -> (short) ((LongColumnVector) columnVector).vector[row];
        } else if ("int".equals(type) || "date".equals(type)) {
            // Date is represented as int epoch days
            mapper = (columnVector, row) -> (int) ((LongColumnVector) columnVector).vector[row];
        } else if ("bigint".equals(type)) {
            mapper = (columnVector, row) -> ((LongColumnVector) columnVector).vector[row];
        } else if ("boolean".equals(type)) {
            mapper = (columnVector, row) -> ((LongColumnVector) columnVector).vector[row] == 1;
        } else if ("float".equals(type)) {
            mapper = (columnVector, row) -> (float) ((DoubleColumnVector) columnVector).vector[row];
        } else if ("double".equals(type)) {
            mapper = (columnVector, row) -> ((DoubleColumnVector) columnVector).vector[row];
        } else if ("decimal".equals(type)) {
            mapper = (columnVector, row) -> ((DecimalColumnVector) columnVector).vector[row].getHiveDecimal().bigDecimalValue();
        } else if ("string".equals(type) || type.startsWith("varchar")) {
            mapper = (columnVector, row) -> ((BytesColumnVector) columnVector).toString(row);
        } else if ("char".equals(type)) {
            mapper = (columnVector, row) -> ((BytesColumnVector) columnVector).toString(row).charAt(0);
        } else if ("timestamp".equals(type)) {
            mapper = (columnVector, row) -> ((TimestampColumnVector) columnVector).getTimestampAsLong(row);
        } else {
            throw new RuntimeException("Unsupported type " + type);
        }
        return mapper;
    }
}