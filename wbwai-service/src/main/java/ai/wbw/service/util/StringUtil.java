package ai.wbw.service.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.util.Random;

/**
 * @Description 字符串工具类
 */
public class StringUtil {

    /**
     *
     * 方法的描述: 获得ip段
     * @param ip
     * @return
     */
    public static String getIPSection(String ip) {
        String str = null;
        if (ip != null && !"".equals(ip.trim())) {

            String [] s = ip.split("[.]");
            if (s != null && s.length == 4) {
                str = s[2];
            }
        }
        return str;
    }
    /**
     *
     * 方法的描述: 将null转为空字符串
     * @param s
     * @return
     */
    public static String covertNullToEmpty(String s)
    {
        if(s==null)
        {
            return "";
        }
        return s.trim();
    }

    /**
     * 生成随机数
     * @param length 位数，位数不大于9
     * @return length位随机数
     */
    public static String randomNum(int length){
        if(length>9){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<length-1;i++){
            sb.append("0");
        }
        int x = Integer.parseInt("9"+sb.toString());
        int y = Integer.parseInt("1"+sb.toString());
        Integer i = new Random().nextInt(x)+y;
        return i.toString();
    }

    /**
     * 将10进制num转换为length位数16进制
     * @param length 位数，位数不大于9
     * @return length位随机数
     */
    public static String toHex(Long num,int length){
        StringBuilder sb = new StringBuilder();
        String x = Long.toHexString(num).toUpperCase();
        if(length>x.length())
            for(int i=x.length();i<length;i++){
                sb.append("0");
            }
        sb.append(x);
        return sb.toString();
    }

    /**
     * 转换mac地址去掉冒号
     * @param str
     * @return
     */
    public static String coverMacToString(String str){
        if (str!=null && !"".equals(str)){
            return str.replace(":", "");
        }
        return null;
    }

    /**
     * 校验邮箱格式
     * @param str
     * @return
     */
    public static boolean validateEmail(String str) {
        // TODO Auto-generated method stub
        String regex ="[a-zA-Z0-9]+[a-zA-Z0-9.]+[a-zA-Z0-9]+@[a-zA-Z0-9]+[a-zA-Z0-9.]+[a-zA-Z0-9]+";
        return str.matches(regex);
    }

    public static boolean isJson(String content) {
        if(content!=null && !"".equals(content)) {
            try {
                JSONObject.parseObject(content);
                return true;

            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     *  读取本地html文件里的html代码
     * @return
     */
    public static String toHtmlString(String filePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        // 获取HTML文件流
        StringBuffer htmlSb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inputStream, "UTF-8"));
            while (br.ready()) {
                htmlSb.append(br.readLine());
            }
            br.close();
            // 删除临时文件
            //file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // HTML文件字符串
        String htmlStr = htmlSb.toString();
        // 返回经过清洁的html文本
        return htmlStr;
    }

    /**
     * 判断字符串是否为空或者空白字符串或者"null"
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        return (StringUtils.isBlank(cs) || "null".equals(cs));
    }

    public static void main(String[] args){
//        String s = toHex(123L,1);
//        System.out.println(s);
        System.out.println(validateEmail("christoph.er.bell@aes.globalonline.com"));

//        System.out.println(new Date().getTime());
    }
}
