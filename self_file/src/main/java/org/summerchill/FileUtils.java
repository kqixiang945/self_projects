package org.summerchill;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.summerchill.utils.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtils {

    public static void main(String[] args) throws Exception {
        //delete a directory
        deleteDirectory(new File("/Users/kongxiaohan/Desktop/test2/"));
        org.apache.commons.io.FileUtils.deleteDirectory(new File("/Users/kongxiaohan/Desktop/test2/"));

        //Create a directory if it does not exist and then create the files in that directory as well
        createDirectories("/Users/kongxiaohan/Desktop/test/test2/test3");

        //check a file exits
        whetherFileExits("/Users/kongxiaohan/Desktop");

        //read all files in a folder
        readAllfileInFolder2("/Users/kongxiaohan/Desktop");
        //readAllfileInFolder("/Users/kongxiaohan/Desktop");

        //判断一个文件的文件类型
        getContentType("/Users/kongxiaohan/Desktop/截屏2021-06-19 22.14.38.png");

        //压缩一个文件夹 为一个文件
        zipFile("/home/etluser/app/azkaban_conn/judge_db_con_azkaban/", "/home/etluser/app/azkaban_conn/Project_Hourly_02.zip");
        //读取一个文件到一个字符串中
        String fileContent = readLineByLine("/path1/path2");

        putDataFile2HDFS(new File("filePath"), "target_hdfs_path");
        //读取一个文件的内容到一个list集合中
        Files.readLines(new File("filePath"), Charsets.UTF_8);
        //加载一个数据文件到hive表中
        loadDataPartition2HiveTable("hdfs_file_path", "hive_db_name", "hive_table_name");
        //把内容写入到文件中
        writeContentToFile("target_file_path", "content");

        //read a file content to list
        String filePath = "/Users/kongxiaohan/Desktop/connection.sh";
        readFileContent2List1(filePath);
        readFileContent2List2(filePath);
        readFileContent2List3(filePath);

    }

    /**
     * Classic BufferedReader example
     * @param filePath
     * @throws IOException
     *
     */
    private static void readFileContent2List3(String filePath) throws IOException {
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }


    /**
     * Java 7
     *
     * @param filePath
     */
    private static void readFileContent2List2(String filePath) throws IOException {
        java.nio.file.Files.readAllLines(new File(filePath).toPath(), Charset.defaultCharset());
    }

    /**
     * Java 8 stream
     *
     * @param fileNamePath
     * @return
     * @throws IOException
     */
    private static List<String> readFileContent2List1(String fileNamePath) throws IOException {
        List<String> result;
        try (Stream<String> lines = java.nio.file.Files.lines(Paths.get(fileNamePath))) {
            result = lines.collect(Collectors.toList());
        }
        return result;
    }


    /**
     * detele a directory total (contain sub directory and files)
     *
     * @param directoryToBeDeleted
     * @return
     */
    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    /**
     * Create a directory if it does not exist and then create the files in that directory as well
     *
     * @param path
     * @throws IOException
     */
    private static void createDirectories(String path) throws IOException {
        java.nio.file.Files.createDirectories(Paths.get(path));
        // If you require it to make the entire directory path including parents,use directory.mkdirs(); here instead.
        // also have a directory.mkdir()
    }


    /**
     * judge a file whether exists
     *
     * @param filePathString
     */
    private static void whetherFileExits(String filePathString) {
        File f = new File(filePathString);
        if (f.exists() && !f.isDirectory()) {
            // do something
        }

        //In case of just for existence. It could be file or a directory.
        new File("/path/to/file").exists();

        //Check for file
        File f1 = new File("/path/to/file");
        if (f.exists() && f.isFile()) {
        }

        //Check for Directory.
        File f2 = new File("/path/to/file");
        if (f.exists() && f.isDirectory()) {
        }

        //Java 7 way.
        java.nio.file.Path path = Paths.get("/path/to/file");
        java.nio.file.Files.exists(path);  // Existence
        java.nio.file.Files.isDirectory(path);  // is Directory
        java.nio.file.Files.isRegularFile(path);  // Regular file
        java.nio.file.Files.isSymbolicLink(path);  // Symbolic Link
    }

    /**
     * read all files list in a folder
     *
     * @param path
     */
    private static void readAllfileInFolder(String path) {
        final File folder = new File(path);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                readAllfileInFolder(fileEntry.getAbsolutePath());
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

    /**
     * read all files list in a folder
     * About Files.walk:
     * The returned stream encapsulates one or more DirectoryStreams.
     * If timely disposal of file system resources is required, the try-with-resources construct should be used to ensure that
     * the stream's close method is invoked after the stream operations are completed.
     *
     * @param path
     */
    private static void readAllfileInFolder2(String path) throws IOException {
        //will print all files in a folder while excluding all directories
        java.nio.file.Files.walk(Paths.get(path))
                .filter(java.nio.file.Files::isRegularFile)
                .forEach(System.out::println);

        //If you need a list, the following will do:
        java.nio.file.Files.walk(Paths.get(path))
                .filter(java.nio.file.Files::isRegularFile)
                .collect(Collectors.toList());

        //If you want to return List<File> instead of List<Path> just map it:
        List<File> filesInFolder = java.nio.file.Files.walk(Paths.get(path))
                .filter(java.nio.file.Files::isRegularFile)
                .map(java.nio.file.Path::toFile)
                .collect(Collectors.toList());

        try (Stream<java.nio.file.Path> filePathStream = java.nio.file.Files.walk(Paths.get(path))) {
            filePathStream.forEach(filePath -> {
                if (java.nio.file.Files.isRegularFile(filePath)) {
                    System.out.println(filePath);
                }
            });
        }
    }


    /**
     * BufferedReader convert to String.
     * https://stackoverflow.com/questions/15040504/how-to-easily-convert-a-bufferedreader-to-a-string
     *
     * @param bufferedReader
     * @return
     * @throws IOException
     */
    public static String bufferReaderConvertToString(BufferedReader bufferedReader) throws IOException {
        //method_1:java8
        //Collectors.joining("\n") to add a trailing newline character to each line. if do not set '\n' all content will be in a line.
        String fileString = bufferedReader.lines().collect(Collectors.joining("\n"));

        //method_2:commons IO library
        String message = org.apache.commons.io.IOUtils.toString(bufferedReader);

        //method_3:core java
        String response = new String();
        for (String line; (line = bufferedReader.readLine()) != null; response += line) ;

        return fileString;
    }


    /**
     * 根据文件路径判断文件类型:
     * https://blog.csdn.net/weixin_46288569/article/details/106582181
     * 自己尝试NIO的probeContentType方法发现几乎都是返回null
     *
     * @param filePath
     * @return
     */
    public static String getContentType(String filePath) {
        //利用nio提供的类判断文件ContentType
        java.nio.file.Path path = Paths.get(filePath);
        String content_type = null;
        try {
            content_type = java.nio.file.Files.probeContentType(path);
        } catch (IOException e) {
            System.out.println("Read File ContentType Error");
        }
        //若失败则调用另一个方法进行判断
        if (content_type == null) {
            content_type = new MimetypesFileTypeMap().getContentType(filePath);
        }
        return content_type;
    }

    /**
     * 判断文件类型, 更推荐上面的getContentType 方法.
     *
     * @param fileName
     * @return
     */
    public static String identifyFileTypeUsingMimetypesFileTypeMap(String fileName) {
        final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        System.out.println(fileTypeMap.getContentType(fileName));
        return fileTypeMap.getContentType(fileName);
    }

    /**
     * 把指定的字符串写入到指定的文件中
     *
     * @param filePath
     * @param content
     * @throws IOException
     */
    public static void writeContentToFile(String filePath, String content) throws IOException {
        CharSink scriptSink = Files.asCharSink(new File(filePath), Charsets.UTF_8);
        scriptSink.write(content);
    }

    /**
     * 读取一个文件的内容到一个字符串中
     *
     * @param filePath
     * @return
     */
    private static String readLineByLine(String filePath) {
        try {
            File file = new File(filePath);
            return org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("读取文件:" + filePath + "发生异常,请检查!");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param sourceFilePath    要压缩的文件(目录)
     * @param targetZipFilePath 要压缩成的目标文件绝对路径.
     */
    public static void zipFile(String sourceFilePath, String targetZipFilePath) {
        try {
            FileOutputStream fos = new FileOutputStream(targetZipFilePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File sourceFile = new File(sourceFilePath);
            zipFileProcess(sourceFile, sourceFile.getName(), zipOut);
            zipOut.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param sourceFile
     * @param sourceFileName
     * @param zipOut
     * @throws IOException
     */
    public static void zipFileProcess(File sourceFile, String sourceFileName, ZipOutputStream zipOut) throws IOException {
        if (sourceFile.isHidden()) {
            return;
        }
        if (sourceFile.isDirectory()) {
            if (sourceFileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(sourceFileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(sourceFileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = sourceFile.listFiles();
            for (File childFile : children) {
                zipFileProcess(childFile, sourceFileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(sourceFile);
        ZipEntry zipEntry = new ZipEntry(sourceFileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    /**
     * 把该文件上传到指定的HDFS目录上(Hive表中),并把csv数据文件加载到Hive表中. 如果指定的HDFS路径不存在需要创建(分区文件夹)
     *
     * @param dataFile
     * @param hdfsPath 是HDFS上的一个目录(表的hdfs目录)
     */
    public static void putDataFile2HDFS(File dataFile, String hdfsPath) {
        try {
            log.info("开始上传本地数据文件" + dataFile.getAbsolutePath() + "到HDFS上的" + hdfsPath + "中..");
            String HDFS_ROOT_URI = StringUtils.getTargetStrUseRegEx(hdfsPath, "hdfs://([A-Za-z0-9_.:]+)");

            FileSystem fileSystem = getFileSystem(HDFS_ROOT_URI);
            //本地上传文件路径
            Path srcPath = new Path(dataFile.getAbsolutePath());

            log.info("最终目标路径为:" + hdfsPath);
            //hdfs目标路径
            Path dstPath = new Path(hdfsPath);
            if (!fileSystem.exists(dstPath)) {
                fileSystem.mkdirs(dstPath);
            }
            //调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
            fileSystem.copyFromLocalFile(false, srcPath, dstPath);

            //HDFS目标文件夹下的所有文件.
            printAllFilesUnderHdfsDir(fileSystem, hdfsPath);
            fileSystem.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 打印出指定hdfs目录路径下的所有文件
     *
     * @param fileSystem
     * @param hdfsPath
     */
    public static void printAllFilesUnderHdfsDir(FileSystem fileSystem, String hdfsPath) {
        try {
            //hdfs目标路径
            Path dstPath = new Path(hdfsPath);
            log.info("-----HDFS的路径" + hdfsPath + "路径下的所有文件如下:------------");
            FileStatus[] fileStatus = fileSystem.listStatus(dstPath);
            for (FileStatus file : fileStatus) {
                log.info(file.getPath().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到HDFS 分布式文件系统的 核心FileSystem类
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private static FileSystem getFileSystem(String hdfsRootUri) throws URISyntaxException, IOException {
        Configuration conf = new Configuration();
        //需要做如下设置  否则无法操作HDFS
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        //注意这个是java.net.URI;
        URI uri = new URI(hdfsRootUri);
        System.setProperty("HADOOP_USER_NAME", "kaifa");
        final FileSystem fileSystem = FileSystem.get(uri, conf);
        return fileSystem;
    }

    /**
     * 通过库名,表名,和hdfs路径 加载数据文件到Hive表中.
     *
     * @param fileHDFSDir
     * @param hiveDbName
     * @param hiveTableName
     * @throws Exception
     */

    public static void loadDataPartition2HiveTable(String fileHDFSDir, String hiveDbName, String hiveTableName) {
        try {
            Class.forName("database_jdbc_class");
            Connection hiveDbConn = DriverManager.getConnection("hive_jdbc_url", "hive_user_name", "hive_password");
            Statement stmt = hiveDbConn.createStatement();
            //得到分区名 最后一级目录
            if (fileHDFSDir.endsWith("/")) {
                fileHDFSDir = fileHDFSDir.substring(0, fileHDFSDir.length() - 1);
            }
            fileHDFSDir = fileHDFSDir.substring(fileHDFSDir.lastIndexOf("/") + 1);
            StringBuilder sqlSb = new StringBuilder();
            //ALTER TABLE ${HIVE_DB_ODS_ALPHA}.contract_histories ADD IF NOT EXISTS PARTITION(load_date='${CURRENT_FLOW_START_DAY}
            sqlSb.append("ALTER TABLE " + hiveDbName + "." + hiveTableName + " ADD IF NOT EXISTS PARTITION(load_date"
                    + "='" + fileHDFSDir.split("=")[1] + "')");
            log.info("执行加载分区的SQL为:" + sqlSb.toString());
            //关于这个execute方法的返回值:
            //return true if the first result is a ResultSet object;   return false if it is an update count or there are no results.
            //对于现在的情况肯定是返回的false... 如果出错肯定是抛出异常了...
            stmt.execute(sqlSb.toString());
            stmt.close();
            hiveDbConn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 获取没有后缀名的文件名
     * https://stackoverflow.com/questions/924394/how-to-get-the-filename-without-the-extension-in-java
     * 使用工具类
     *
     * @param fileName
     */
    public static String getFileNameWithoutExtentsion(String fileName) {
        //Using apache commons
        String fileNameWithoutExt_1 = FilenameUtils.getBaseName(fileName);
        String fileNameWithOutExt_2 = FilenameUtils.removeExtension(fileName);

        //Using Google Guava (If u already using it)
        String fileNameWithOutExt_3 = Files.getNameWithoutExtension(fileName);


        return fileNameWithOutExt_3;
    }

    /**
     * 使用核心java的方式
     *
     * @param file
     * @return
     */
    public static String getFileNameWithoutExtentsion(File file) {
        //method 1
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }
        //method 2
        Pattern ext = Pattern.compile("(?<=.)\\.[^.]+$");
        fileName = ext.matcher(file.getName()).replaceAll("");

        //method 3
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }

}
