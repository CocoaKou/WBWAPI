package ai.wbw.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ai.wbw.service.common.enums.Code;
import ai.wbw.service.common.model.ResultEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description Http工具类
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static ResultEntity<String> doGet(String url, Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        ResultEntity<String> result = new ResultEntity<>(Code.FAILED.getValue(),Code.FAILED.getMsg(),"");
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                result.setCode(Code.SUCCESS.getValue());
                result.setMsg(Code.SUCCESS.getMsg());
            }
            result.setData(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static ResultEntity<String> doGet(String url) {
        return doGet(url, null);
    }

    public static ResultEntity<String> doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        ResultEntity<String> result = new ResultEntity<>(Code.FAILED.getValue(),Code.FAILED.getMsg(),"");
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result.setCode(Code.SUCCESS.getValue());
                result.setMsg(Code.SUCCESS.getMsg());
            }
            result.setData(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    public static ResultEntity<String> doPost(String url) {
        return doPost(url, null);
    }

    public static ResultEntity<String> doPostJson(String url, String json) {
        return doPostJson(url,json,0);
    }

    /**
     * json application post
     * @param url
     * @param json
     * @param timeout ms
     * @return
     */
    public static ResultEntity<String> doPostJson(String url, String json, int timeout) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        ResultEntity<String> result = new ResultEntity<>(Code.FAILED.getValue(),Code.FAILED.getMsg(),"");
        try {
            logger.debug("post [ url:{},param:{} ]",url,json);
            if(timeout == 0){
                timeout = 5000;
            }
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            //建立连接的时间
            customReqConf.setConnectTimeout(timeout);
            //数据传输过程中数据包间隔时间
//            customReqConf.setSocketTimeout(timeout);
            //从连接池获取连接的时间
//            customReqConf.setConnectionRequestTimeout(timeout);
            httpPost.setConfig(customReqConf.build());
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result.setCode(Code.SUCCESS.getValue());
                result.setMsg(Code.SUCCESS.getMsg());
                logger.debug("post success [ url:{} ]",url);
            }
            result.setData(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            logger.error("post failed [ url:{} ]",url);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if(response != null) {
                    response.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * JSON响应
     */
    private static void respondJson(HttpServletResponse response, ResultEntity<String> resultEntity) {
        if(null == resultEntity){
            resultEntity = new ResultEntity<String>();
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String json = new ObjectMapper().writeValueAsString(resultEntity);
            out.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
        }
    }

    /**
     * REST失败响应
     */
    public static void restFailed(HttpServletResponse response, ResultEntity<String> resultEntity) {
        respondJson(response,resultEntity);
    }
}
