package org.summerchill.orc;

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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ORCReaderAndWriter {

    public static void main(String[] args) throws IOException, ParseException {
        //String filePath = testWrite();
        //readTest(filePath);

        String filePath = "/Users/kongxiaohan/Desktop/000000_0_o";
        readTest(filePath);
    }

    private static String testWrite() throws IOException, ParseException {
        Configuration conf = new Configuration();
        TypeDescription schema = TypeDescription.createStruct()
                .addField("a-string", TypeDescription.createString())
                .addField("b-date", TypeDescription.createDate())
                .addField("c-double", TypeDescription.createDouble())
                .addField("d-time", TypeDescription.createTimestamp())
                .addField("e-string", TypeDescription.createString());
        //TypeDescription schema = TypeDescription.fromString("struct<a:string,b:date,c:double,d:timestamp,e:string>");

        String orcFile = System.getProperty("java.io.tmpdir") + File.separator + "orc-test-" + System.currentTimeMillis() + ".orc";

        if (Files.exists(Paths.get(orcFile))) {
            Files.delete(Paths.get(orcFile));
        }

        Writer writer = OrcFile.createWriter(new Path(orcFile),
                OrcFile.writerOptions(conf)
                        .setSchema(schema));

        VectorizedRowBatch batch = schema.createRowBatch();
        BytesColumnVector a = (BytesColumnVector) batch.cols[0];
        LongColumnVector b = (LongColumnVector) batch.cols[1];
        DoubleColumnVector c = (DoubleColumnVector) batch.cols[2];
        TimestampColumnVector d = (TimestampColumnVector) batch.cols[3];
        BytesColumnVector e = (BytesColumnVector) batch.cols[4];
        for (int r = 0; r < 500; ++r) {
            int row = batch.size++;
            a.setVal(row, ("a-" + r).getBytes());
            b.vector[row] = LocalDate.parse("2019-07-22").minusDays(r).toEpochDay();
            c.vector[row] = Double.valueOf(r);
            d.set(row, new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2019-07-22T01:12:37.758-0500").getTime()));
            e.setVal(row, ("e-" + r).getBytes());
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