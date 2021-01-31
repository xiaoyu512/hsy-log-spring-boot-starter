package com.hsy.log.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.io.IOException;

/**
 * JSON与OBJ之间的转换
 *
 * @author HuoShengyu
 * @version 1.0
 * @date 2018-02-08 09:49:00
 */
public class JSONUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
    }

    /**
     * obj转换成json
     *
     * @param object 对象
     * @return String
     */
    public static String toJson(Object object) {
        String jsonString = null;
        try {
            jsonString = MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("object to json error!", e);
        }
        return jsonString;
    }

    /**
     * obj转换成bytes
     *
     * @param object 对象
     * @return byte[]
     */
    public static byte[] toBytes(Object object) {
        byte[] bytes = null;
        try {
            bytes = MAPPER.writeValueAsBytes(object);
        } catch (IOException e) {
            throw new RuntimeException("object to json error!", e);
        }
        return bytes;
    }
}
