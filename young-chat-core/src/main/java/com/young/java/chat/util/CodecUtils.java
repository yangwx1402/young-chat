package com.young.java.chat.util;

import com.google.common.io.BaseEncoding;

/**
 * Created by young.yang on 2016/11/7.
 */
public class CodecUtils {
    public static String base64Encode(String data){
       return BaseEncoding.base64().encode(data.getBytes());
    }

    public static String base64Decode(String code){
        return new String(BaseEncoding.base64().decode(code));
    }

    public static void main(String[] args){
       String name = "楊勇";
        System.out.println(CodecUtils.base64Encode(name));
        String code = "l+7Twg==";
        System.out.println(CodecUtils.base64Decode(code));
    }
}
