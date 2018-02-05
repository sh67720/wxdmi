package com.wxdmi.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by user on 2018/2/5.
 */
public class Utils {

    //map转成String，用于保存日志
    public static String mapToString(Map<String, String> map){
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            stringBuffer.append(key + ":" + value + "; ");
        }
        return stringBuffer.toString();
    }
}
