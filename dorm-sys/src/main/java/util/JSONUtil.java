package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

//核心作用：对象状态的保存和重建
public class JSONUtil {
    //数据的解析和包装
    public static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static <T> T read(InputStream is, Class<T> clazz) {
        //把字节序列恢复为Java对象的过程
        try {
            return MAPPER.readValue(is,clazz);
        } catch (IOException e) {
            throw new RuntimeException("json反序列化失败",e);
        }
    }

    public static String write(Object o) {
        //序列化：把Java对象转化为字节序列的过程
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json序列化失败",e);
        }
    }
}
