package org.summerchill.orc;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.*;
import org.apache.orc.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ORCReaderAndWriter2 {

    public static void main(String[] args) throws IOException, ParseException {
        String filePath = testWrite();
        System.out.println(filePath);
        readTest(filePath);

        //String filePath = "/Users/kongxiaohan/Desktop/000000_0_o";
        //readTest(filePath);
    }

    private static String testWrite() throws IOException, ParseException {
        Configuration conf = new Configuration();
        TypeDescription schema = TypeDescription.createStruct()
                .addField("systemname_alm-string", TypeDescription.createString())
                .addField("systemname_alm-string", TypeDescription.createString())
                .addField("systemno-string", TypeDescription.createString())
                .addField("systemname-string", TypeDescription.createString())
                .addField("business_alm-string", TypeDescription.createString())
                .addField("job_funciton_alm-string", TypeDescription.createString())
                .addField("center_alm-string", TypeDescription.createString())
                .addField("system_source-string", TypeDescription.createString())
                .addField("extend_1-string", TypeDescription.createString())
                .addField("extend_2-string", TypeDescription.createString())
                .addField("extend_3-string", TypeDescription.createString())
                .addField("extend_4-string", TypeDescription.createString())
                .addField("extend_5-string", TypeDescription.createString())
                .addField("op_time-string", TypeDescription.createString())
                .addField("execution_id-string", TypeDescription.createString())
                .addField("load_date-string", TypeDescription.createString());
        //TypeDescription schema = TypeDescription.fromString("struct<a:string,b:date,c:double,d:timestamp,e:string>");

        String orcFile = System.getProperty("java.io.tmpdir") + File.separator + "orc-test-" + System.currentTimeMillis() + ".orc";

        if (Files.exists(Paths.get(orcFile))) {
            Files.delete(Paths.get(orcFile));
        }

        Writer writer = OrcFile.createWriter(new Path(orcFile), OrcFile.writerOptions(conf).setSchema(schema));

        VectorizedRowBatch batch = schema.createRowBatch();
        BytesColumnVector a0 = (BytesColumnVector) batch.cols[0];
        BytesColumnVector a1 = (BytesColumnVector) batch.cols[1];
        BytesColumnVector a2 = (BytesColumnVector) batch.cols[2];
        BytesColumnVector a3 = (BytesColumnVector) batch.cols[3];
        BytesColumnVector a4 = (BytesColumnVector) batch.cols[4];
        BytesColumnVector a5 = (BytesColumnVector) batch.cols[5];
        BytesColumnVector a6 = (BytesColumnVector) batch.cols[6];
        BytesColumnVector a7 = (BytesColumnVector) batch.cols[7];
        BytesColumnVector a8 = (BytesColumnVector) batch.cols[8];
        BytesColumnVector a9 = (BytesColumnVector) batch.cols[9];
        BytesColumnVector a10 = (BytesColumnVector) batch.cols[10];
        BytesColumnVector a11 = (BytesColumnVector) batch.cols[11];
        BytesColumnVector a12 = (BytesColumnVector) batch.cols[12];
        BytesColumnVector a13 = (BytesColumnVector) batch.cols[13];
        BytesColumnVector a14 = (BytesColumnVector) batch.cols[14];
        BytesColumnVector a15 = (BytesColumnVector) batch.cols[15];

        for (int r = 0; r < 500; ++r) {
            int row = batch.size++;
            a0.setVal(row, ("systemno_alm-string" + r).getBytes());
            a1.setVal(row, ("systemname_alm-string" + r).getBytes());
            a2.setVal(row, ("systemno-string" + r).getBytes());
            a3.setVal(row, ("systemname-string" + r).getBytes());
            a4.setVal(row, ("business_alm-string" + r).getBytes());
            a5.setVal(row, ("job_funciton_alm-string" + r).getBytes());
            a6.setVal(row, ("center_alm-string" + r).getBytes());
            a7.setVal(row, ("system_source-string" + r).getBytes());
            a8.setVal(row, ("extend_1-string" + r).getBytes());
            a9.setVal(row, ("extend_2-string" + r).getBytes());
            a10.setVal(row, ("extend_3-string" + r).getBytes());
            a11.setVal(row, ("extend_4-string" + r).getBytes());
            a12.setVal(row, ("extend_5-string" + r).getBytes());
            a13.setVal(row, ("op_time-string" + r).getBytes());
            a14.setVal(row, ("execution_id-string" + r).getBytes());
            a15.setVal(row, ("load_date-string" + r).getBytes());
            // If the batch is full, write it out and start over.
            if (batch.size == batch.getMaxSize()) {
                writer.addRowBatch(batch);
                batch.reset();
            }
        }
        if (batch.size != 0) {
            writer.addRowBatch(batch);
            batch.reset();
        }
        writer.close();

        return orcFile;
    }

    private static void readTest(String filePath) throws IOException {
        Configuration conf = new Configuration();
        conf.setAllowNullValueProperties(true);
        Reader reader = OrcFile.createReader(new Path(filePath),
                OrcFile.readerOptions(conf));

        RecordReader rows = reader.rows();
        VectorizedRowBatch batch = reader.getSchema().createRowBatch();
        System.out.println("schema:" + reader.getSchema());
        System.out.println("numCols:" + batch.numCols);
        ColumnVector.Type[] colsMap = new ColumnVector.Type[batch.numCols];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        while (rows.nextBatch(batch)) {
            //BytesColumnVector cols0 = (BytesColumnVector) batch.cols[0];
            //LongColumnVector cols1 = (LongColumnVector) batch.cols[1];
            //DoubleColumnVector cols2 = (DoubleColumnVector) batch.cols[2];
            //TimestampColumnVector cols3 = (TimestampColumnVector) batch.cols[3];
            //BytesColumnVector cols4 = (BytesColumnVector) batch.cols[4];

            BytesColumnVector cols0 = (BytesColumnVector) batch.cols[0];
            BytesColumnVector cols1 = (BytesColumnVector) batch.cols[1];
            BytesColumnVector cols2 = (BytesColumnVector) batch.cols[2];
            BytesColumnVector cols3 = (BytesColumnVector) batch.cols[3];
            BytesColumnVector cols4 = (BytesColumnVector) batch.cols[4];
            BytesColumnVector cols5 = (BytesColumnVector) batch.cols[5];
            BytesColumnVector cols6 = (BytesColumnVector) batch.cols[6];
            BytesColumnVector cols7 = (BytesColumnVector) batch.cols[7];
            BytesColumnVector cols8 = (BytesColumnVector) batch.cols[8];
            BytesColumnVector cols9 = (BytesColumnVector) batch.cols[9];
            BytesColumnVector cols10 = (BytesColumnVector) batch.cols[10];
            BytesColumnVector cols11 = (BytesColumnVector) batch.cols[11];
            BytesColumnVector cols12 = (BytesColumnVector) batch.cols[12];
            BytesColumnVector cols13 = (BytesColumnVector) batch.cols[13];
            BytesColumnVector cols14 = (BytesColumnVector) batch.cols[14];
            BytesColumnVector cols15 = (BytesColumnVector) batch.cols[15];


            for (int cols = 0; cols < batch.numCols; cols++) {
                System.out.println("args = [" + batch.cols[cols].type + "]");
            }

            for (int r = 0; r < batch.size; r++) {
                //String a = cols0.toString(r);
                //        System.out.println("date:" + cols1.vector[r]);
                //              String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(cols1.vector[r]));
                //              String value2 = String.valueOf(cols1.vector[r]);
                //String b = LocalDate.ofEpochDay(cols1.vector[r]).atStartOfDay(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                //              System.out.println("date:" + date);

                //Double c = cols2.vector[r];
                //Timestamp d = cols3.asScratchTimestamp(r);
                //String e = cols4.toString(r);

                //              String timeV = new String(insertTime.vector[r], insertTime.start[r], insertTime.length[r]);
                //              String value2 = jobId.length[r] == 0 ? "": new String(jobId.vector[r], jobId.start[r], jobId.length[r]);



                String a0 = cols0.toString(r);
                String a1 = cols1.toString(r);
                String a2 = cols2.toString(r);
                String a3 = cols3.toString(r);
                String a4 = cols4.toString(r);
                String a5 = cols5.toString(r);
                String a6 = cols6.toString(r);
                String a7 = cols7.toString(r);
                String a8 = cols8.toString(r);
                String a9 = cols9.toString(r);
                String a10 = cols10.toString(r);
                String a11 = cols11.toString(r);
                String a12 = cols12.toString(r);
                String a13 = cols13.toString(r);
                String a14 = cols14.toString(r);
                String a15 = cols15.toString(r);
                System.out.println(a1 + ", " + a2);


            }
        }
        rows.close();
    }


}