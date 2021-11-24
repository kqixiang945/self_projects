package org.summerchill;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpUtils {

    public static void main(String[] args) {
        //1.发送post请求

        //2.发送get请求


        //3.发送put请求


        //4.使用curl发送各种http请求



    }

    /**
     * 样例: cmd:curl --insecure -H "Content-Type: multipart/mixed" -X POST --form 'session.id=7f46a7b6-a3d2-4d07-8a8f-3f330cb8534d' --form 'ajax=upload' --form 'file=@/home/etluser/app/metacenter/meta_exec_server/azkabanProjectDir/test_lzb_20200811145208.zip;type=application/zip' --form 'project=test_lzb' https://10.240.4.8:8443/manager
     *
     * @param projectName
     * @param projectZipFile
     * @param sessionId
     * @return
     */
    public static void uploadZipFile2Azkaban(String projectName, String projectZipFile, String sessionId) {
        System.out.println("开始上传zip文件" + projectZipFile + "到Azkanban的Project_Hourly_02中....");
        StringBuilder cmdSb = new StringBuilder();
        cmdSb.append("");
        CommandExecutor ce = new CommandExecutor();
        try {
            System.out.println("运行的上传azkaban的shell命令是:" + cmdSb.toString());
            ce.runCmd(cmdSb.toString(), "/指定要执行这个命令的路径");
        } catch (Exception e) {
            System.out.println(projectName + " uploading failed due to \n " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Project " + projectName + " has been uploaded to azkaban");
    }


    /**
     * @param urlStr          Post的请求地址Url
     * @param postContentType Post请求的类型
     * @param postStr
     * @return
     */
    public static String sendPost(String urlStr, String postContentType, String postStr, String bizType) {
        try {
            HttpPost httpPost = new HttpPost("");
            httpPost.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(urlStr);
            StringEntity paramsEntity = null;
            if ("application/json".equals(postContentType)) {
                request.addHeader("Content-Type", postContentType);
                //不同的业务类型对http的header中的设置不同:
                if ("biz_type_****".equals(bizType)) {
                    request.addHeader("refer", "app");
                    request.addHeader("apiversion", "1.1");
                    //目前如下c3_contract_rent_cost业务类型需要在header中设置如下内容(对方接口目前没有真正的做header中的校验,先写死):
                } else if ("biz_type_****".equals(bizType)) {
                    request.addHeader("appID", "****");
                    request.addHeader("keySecret", "****");
                    request.addHeader("date", "****");
                    request.addHeader("authorization", "Basic RDNDRUJBNDE1QTEyOjIwMTkwNTA4OkMyQkY5QjQyODU4NzlFMw==");
                    //c3_contract_rent_cost 需要在header中设置header如下:
                } else if ("biz_type_****".equals(bizType)) {
                    request.addHeader("token", "*****");
                    request.addHeader("req_time", "*****");
                    request.addHeader("auth", "*****");
                    //需要有查询参数的业务类型需要加入if条件中
                } else if ("biz_type_****".equals(bizType)) {
                    long timestamp = System.currentTimeMillis();
                    String secretKey = "*****";
                    String token = "****";
                    request.addHeader("refer", "*****");
                    request.addHeader("X-Gaia-Api-Key", "*****");
                    postStr = "{\n" +
                            "\"token\":\"" + token + "\",\n" +
                            "\"timestamp\":" + timestamp + "\n" +
                            "}";
                }
                // 设置请求的参数
                JSONObject jsonParam = new JSONObject(postStr);
                paramsEntity = new StringEntity(jsonParam.toString(), "utf-8");
                request.setEntity(paramsEntity);
            } else if ("application/x-www-form-urlencoded".equals(postContentType)) {
                request.addHeader("Content-Type", postContentType);
                //对不同的类型有不同的处理方式
                //能耗接口的判断
                if ("biz_type_****".equals(bizType)) {
                    paramsEntity = new StringEntity(postStr);
                } else if ("biz_type_****".equals(bizType)) {
                    //微信广告数据接口的处理 Request parameters and other properties.
                    List<NameValuePair> params = new ArrayList<>();
                    //该字段是名字 目前采用 AD_DATA_20191213131313精确到秒的形式
                    params.add(new BasicNameValuePair("*****", "AD_DATA_" + "******"));
                    params.add(new BasicNameValuePair("*****", "******"));
                    String key3Value3 = "";
                    //如果param非空
                    if (!Strings.isNullOrEmpty("*****")) {
                        //组装json串
                        key3Value3 = "****";
                    } else {
                        //组装json串
                        key3Value3 = "****";
                    }
                    log.info("请求ad_data数据对应的task_spec的具体值为：" + key3Value3);
                    //封装第三个form-data项
                    //这个参数是用来设置请求时间和请求返回的字段的，里面的时间最多可以设置7天的时间间隔。
                    params.add(new BasicNameValuePair("*****", key3Value3));
                    paramsEntity = new UrlEncodedFormEntity(params, "UTF-8");
                } else if ("biz_type_****".equals(bizType)) {
                    // 微信广告效果数据日报接口的处理 Request parameters and other properties.
                    List<NameValuePair> params = new ArrayList<>();
                    //该字段是名字 目前采用 AD_EFFECT_DATA_DAILY_REPORT_20191213131313精确到秒的形式
                    params.add(new BasicNameValuePair("*****", "AD_EFFECT_DATA_DAILY_REPORT_" + "*****"));
                    params.add(new BasicNameValuePair("*****", "******"));
                    LocalDate localDate = null;
                    String key3Value3 = "";

                    //如果param非空，开始初始化的时候要获取全量数据
                    if (!Strings.isNullOrEmpty("*****")) {
                        //在param非空的 情况下有可能会有循环请求，根据AdDataAction中的成员变量loopCount来动态计算时间

                    } else {
                        //如果param为空 那么就取得CURRENT_FLOW_START_DAY,对应请求的日期是该日期的前一天

                    }
                    log.info("请求ad_effect_data_daily_report数据对应的task_spec的具体值为：" + key3Value3);
                    //封装第三个form-data项
                    params.add(new BasicNameValuePair("******", key3Value3));
                    paramsEntity = new UrlEncodedFormEntity(params, "UTF-8");
                } else {
                    log.error("你要处理的类型：" + bizType + "post请求有误，请检查！");
                }
                request.setEntity(paramsEntity);
            } else if ("multipart/form-data".equals(postContentType)) {
                if ("biz_type_aaa".equals(bizType)) {
                    //创建 MultipartEntityBuilder,以此来构建我们的参数
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    //设置字符编码，防止乱码
                    ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
                    //填充我们的文本内容，这里相当于input 框中的 name 与value
                    builder.addPart("Mdmid", new StringBody(postStr, contentType));
                    builder.addPart("AccountCode", new StringBody("", contentType));
                    builder.addPart("PageIndex", new StringBody("0", contentType));
                    builder.addPart("PageSize", new StringBody("1000000", contentType));
                    request.addHeader("X-Gaia-Api-Key", "key_value_***");
                    //参数组装
                    request.setEntity(builder.build());
                }
            }
            HttpResponse response = httpClient.execute(request);

            if (response != null) {
                //Get the data in the entity
                InputStream inputStream = response.getEntity().getContent();
                StringBuilder responseJsonStr = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                //对方返回的就一行或者多行的JSON串
                while ((line = reader.readLine()) != null) {
                    responseJsonStr.append(line);
                }
                log.info("查询的URL是:" + urlStr + "; 对应的查询参数为:" + postStr + ";对应的返回值为:" + responseJsonStr);
                return responseJsonStr.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    public static String sendPut(String url, String postContentType) {
        String responseData = "";
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPut request = new HttpPut(url);
            request.addHeader("Content-Type", postContentType);
            String json = "*****";
            StringEntity paramsEntity = new StringEntity(json, "UTF-8");
            request.setEntity(paramsEntity);

            HttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse != null && 200 == httpResponse.getStatusLine().getStatusCode()) {
                responseData = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return
     */
    public static String sendGet(String url, Map<String, String> headerMap) throws Exception {
        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.生成一个get请求
        HttpGet httpget = new HttpGet(url);
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpget.setHeader(entry.getKey(), entry.getValue());
        }
        CloseableHttpResponse response = null;
        try {
            //3.执行get请求并返回结果
            response = (CloseableHttpResponse) httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(-1);
        }
        String result = null;
        try {
            //4.处理结果，这里将结果返回为字符串
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return result;
    }
}