package org.summerchill.hdfs;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author kxh
 * @description 读写orc格式的文件
 * @date 20210624_15:36
 */
public class HdfsFileUtils {

    public static final Logger LOG = LoggerFactory.getLogger(HdfsFileUtils.class);
    public FileSystem fileSystem = null;
    public JobConf conf = null;
    private String fileName;
    public org.apache.hadoop.conf.Configuration hadoopConf = null;
    public static final String HDFS_DEFAULTFS_KEY = "fs.defaultFS";

    public static void main(String[] args) {


    }

    public void getFileSystem(String defaultFS) {
        hadoopConf = new org.apache.hadoop.conf.Configuration();
        hadoopConf.set(HDFS_DEFAULTFS_KEY, defaultFS);
        conf = new JobConf(hadoopConf);
        try {
            fileSystem = FileSystem.get(conf);
        } catch (IOException e) {
            String message = String.format("获取FileSystem时发生网络IO异常,请检查您的网络是否正常!HDFS地址：[%s]", "message:defaultFS =" + defaultFS);
            LOG.error(message);
            throw DataXException.asDataXException(HdfsWriterErrorCode.CONNECT_HDFS_IO_ERROR, e);
        } catch (Exception e) {
            String message = String.format("获取FileSystem失败,请检查HDFS地址是否正确: [%s]", "message:defaultFS =" + defaultFS);
            LOG.error(message);
            throw DataXException.asDataXException(HdfsWriterErrorCode.CONNECT_HDFS_IO_ERROR, e);
        }
        if (null == fileSystem || null == conf) {
            String message = String.format("获取FileSystem失败,请检查HDFS地址是否正确: [%s]", "message:defaultFS =" + defaultFS);
            LOG.error(message);
            throw DataXException.asDataXException(HdfsWriterErrorCode.CONNECT_HDFS_IO_ERROR, message);
        }
    }

    //public void parquetFileWrite() {
    //    // construct parquet schema
    //    String strschema = "{"
    //            + "\"type\": \"record\"," //Must be set as record
    //            + "\"name\": \"dataxFile\"," //Not used in Parquet, can put anything
    //            + "\"fields\": [";
    //    String filedName;
    //    String type;
    //
    //    Path path = new Path(fileName);
    //    LOG.info("write parquet file {}", fileName);
    //    strschema = strschema.substring(0, strschema.length() - 1) + " ]}";
    //    Schema.Parser parser = new Schema.Parser().setValidate(true);
    //    Schema parSchema = parser.parse(strschema);
    //
    //    GenericData decimalSupport = new GenericData();
    //    decimalSupport.addLogicalTypeConversion(new Conversions.DecimalConversion());
    //    try (ParquetWriter<GenericRecord> writer = AvroParquetWriter
    //            .<GenericRecord>builder(path)
    //            .withRowGroupSize(ParquetWriter.DEFAULT_BLOCK_SIZE)
    //            .withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)
    //            .withSchema(parSchema)
    //            .withConf(hadoopConf)
    //            .withCompressionCodec(codecName)
    //            .withValidation(false)
    //            .withDictionaryEncoding(false)
    //            .withDataModel(decimalSupport)
    //            .withWriterVersion(ParquetProperties.WriterVersion.PARQUET_1_0)
    //            .build()) {
    //
    //        Record record;
    //        while ((record = lineReceiver.getFromReader()) != null) {
    //            GenericRecordBuilder builder = new GenericRecordBuilder(parSchema);
    //            GenericRecord transportResult = transportParRecord(record, columns, taskPluginCollector, builder);
    //            writer.write(transportResult);
    //        }
    //    } catch (Exception e) {
    //        LOG.error("写文件文件[{}]时发生IO异常,请检查您的网络是否正常！", fileName);
    //        deleteDir(path.getParent());
    //        throw DataXException.asDataXException(HdfsWriterErrorCode.Write_FILE_IO_ERROR, e);
    //    }
    //}

    public void orcFileStartWrite() {
        try {
            OrcSerde orcSerde = new OrcSerde();
            FileOutputFormat outFormat = new OrcOutputFormat();
            RecordWriter writer = outFormat.getRecordWriter(fileSystem, conf, fileName, Reporter.NULL);
            //Record record = lineReceiver.getFromReader();
            //MutablePair<List<Object>, Boolean> transportResult = transportOneRecord(record, columns, taskPluginCollector);
            //writer.write(NullWritable.get(), orcSerde.serialize(transportResult.getLeft(), inspector));
            writer.close(Reporter.NULL);
        } catch (IOException e) {
            String message = String.format("写文件文件[%s]时发生IO异常,请检查您的网络是否正常！", fileName);
            LOG.error(message);
            Path path = new Path(fileName);
            deleteDir(path.getParent());
            throw DataXException.asDataXException(HdfsWriterErrorCode.Write_FILE_IO_ERROR, e);
        }

    }

    //public void textFileStartWrite() {
    //    Path outputPath = new Path(fileName);
    //    FileOutputFormat outFormat = new TextOutputFormat();
    //    outFormat.setOutputPath(conf, outputPath);
    //    outFormat.setWorkOutputPath(conf, outputPath);
    //    try {
    //        RecordWriter writer = outFormat.getRecordWriter(fileSystem, conf, outputPath.toString(), Reporter.NULL);
    //        writer.write(NullWritable.get(), transportResult.getLeft());
    //        writer.close(Reporter.NULL);
    //    } catch (Exception e) {
    //        String message = String.format("写文件文件[%s]时发生IO异常,请检查您的网络是否正常！", fileName);
    //        LOG.error(message);
    //        Path path = new Path(fileName);
    //        deleteDir(path.getParent());
    //        throw DataXException.asDataXException(HdfsWriterErrorCode.Write_FILE_IO_ERROR, e);
    //    }
    //}

    //public static MutablePair<Text, Boolean> transportOneRecord(
    //        Record record, char fieldDelimiter, List<Configuration> columnsConfiguration, TaskPluginCollector taskPluginCollector) {
    //    MutablePair<List<Object>, Boolean> transportResultList = transportOneRecord(record, columnsConfiguration, taskPluginCollector);
    //    //保存<转换后的数据,是否是脏数据>
    //    MutablePair<Text, Boolean> transportResult = new MutablePair<Text, Boolean>();
    //    transportResult.setRight(false);
    //    if (null != transportResultList) {
    //        Text recordResult = new Text(StringUtils.join(transportResultList.getLeft(), fieldDelimiter));
    //        transportResult.setRight(transportResultList.getRight());
    //        transportResult.setLeft(recordResult);
    //    }
    //    return transportResult;
    //}

    //public static MutablePair<List<Object>, Boolean> transportOneRecord(Record record, List<Configuration> columnsConfiguration,
    //                                                                    TaskPluginCollector taskPluginCollector) {
    //    MutablePair<List<Object>, Boolean> transportResult = new MutablePair<List<Object>, Boolean>();
    //    transportResult.setRight(false);
    //    List<Object> recordList = Lists.newArrayList();
    //    int recordLength = record.getColumnNumber();
    //    if (0 != recordLength) {
    //        Column column;
    //        for (int i = 0; i < recordLength; i++) {
    //            column = record.getColumn(i);
    //            //todo as method
    //            if (null != column.getRawData()) {
    //                String rowData = column.getRawData().toString();
    //                SupportHiveDataType columnType = SupportHiveDataType.valueOf(
    //                        columnsConfiguration.get(i).getString(Key.TYPE).toUpperCase());
    //                //根据writer端类型配置做类型转换
    //                try {
    //                    switch (columnType) {
    //                        case TINYINT:
    //                            recordList.add(Byte.valueOf(rowData));
    //                            break;
    //                        case SMALLINT:
    //                            recordList.add(Short.valueOf(rowData));
    //                            break;
    //                        case INT:
    //                            recordList.add(Integer.valueOf(rowData));
    //                            break;
    //                        case BIGINT:
    //                            recordList.add(column.asLong());
    //                            break;
    //                        case FLOAT:
    //                            recordList.add(Float.valueOf(rowData));
    //                            break;
    //                        case DOUBLE:
    //                            recordList.add(column.asDouble());
    //                            break;
    //                        case STRING:
    //                        case VARCHAR:
    //                        case CHAR:
    //                            recordList.add(column.asString());
    //                            break;
    //                        case BOOLEAN:
    //                            recordList.add(column.asBoolean());
    //                            break;
    //                        case DATE:
    //                            recordList.add(new java.sql.Date(column.asDate().getTime()));
    //                            break;
    //                        case TIMESTAMP:
    //                            recordList.add(new java.sql.Timestamp(column.asDate().getTime()));
    //                            break;
    //                        default:
    //                            throw DataXException
    //                                    .asDataXException(
    //                                            HdfsWriterErrorCode.ILLEGAL_VALUE,
    //                                            String.format(
    //                                                    "您的配置文件中的列配置信息有误. 因为DataX 不支持数据库写入这种字段类型. 字段名:[%s], 字段类型:[%d]. 请修改表中该字段的类型或者不同步该字段.",
    //                                                    columnsConfiguration.get(i).getString(Key.NAME),
    //                                                    columnsConfiguration.get(i).getString(Key.TYPE)));
    //                    }
    //                } catch (Exception e) {
    //                    // warn: 此处认为脏数据
    //                    String message = String.format(
    //                            "字段类型转换错误：你目标字段为[%s]类型，实际字段值为[%s].",
    //                            columnsConfiguration.get(i).getString(Key.TYPE), column.getRawData().toString());
    //                    taskPluginCollector.collectDirtyRecord(record, message);
    //                    transportResult.setRight(true);
    //                    break;
    //                }
    //            } else {
    //                // warn: it's all ok if nullFormat is null
    //                recordList.add(null);
    //            }
    //        }
    //    }
    //    transportResult.setLeft(recordList);
    //    return transportResult;
    //}

    public void deleteDir(Path path) {
        LOG.info(String.format("start delete tmp dir [%s] .", path.toString()));
        try {
            if (isPathexists(path.toString())) {
                fileSystem.delete(path, true);
            }
        } catch (Exception e) {
            String message = String.format("删除临时目录[%s]时发生IO异常,请检查您的网络是否正常！", path.toString());
            LOG.error(message);
            throw DataXException.asDataXException(HdfsWriterErrorCode.CONNECT_HDFS_IO_ERROR, e);
        }
        LOG.info(String.format("finish delete tmp dir [%s] .", path.toString()));
    }

    public boolean isPathexists(String filePath) {
        Path path = new Path(filePath);
        boolean exist = false;
        try {
            exist = fileSystem.exists(path);
        } catch (IOException e) {
            String message = String.format("判断文件路径[%s]是否存在时发生网络IO异常,请检查您的网络是否正常！",
                    "message:filePath =" + filePath);
            LOG.error(message);
            throw DataXException.asDataXException(HdfsWriterErrorCode.CONNECT_HDFS_IO_ERROR, e);
        }
        return exist;
    }
}
