package com.young.java.chat.util;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by dell on 2016/10/31.
 */
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object obj) throws IOException {
       return mapper.writeValueAsString(obj);
    }

    public static <T> T fromJson(String json,Class<T> clazz) throws IOException {
        return mapper.readValue(json,clazz);
    }
}
