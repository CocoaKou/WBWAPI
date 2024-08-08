package ai.wbw.service.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.core.io.ClassPathResource;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Json工具类
 */
public class JsonUtils {

    //读取json文件
    public static JSONObject readJsonFile(String path) {
        try{
            ClassPathResource resource = new ClassPathResource(path);
            Type type = JSONObject.class;
            if (resource.exists()) {
                JSONObject jsonObject = JSON.parseObject(resource.getInputStream(), StandardCharsets.UTF_8,type,
                        // 自动关闭流
                        Feature.AutoCloseSource,
                        // 允许注释
                        Feature.AllowComment,
                        // 允许单引号
                        Feature.AllowSingleQuotes,
                        // 使用 Big decimal
                        Feature.UseBigDecimal);
                return jsonObject;
            } else {
//                throw new IOException();
            }
        } catch(Exception e){

        }
        return null;
    }
}
