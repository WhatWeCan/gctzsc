package com.hdl.gctzsc.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObjectUtils {
    public static Map<String, Object> objToMap(Object object) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        if (object == null) {
            return null;
        }
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = object.getClass().getDeclaredField(
                        fields[i].getName());
                field.setAccessible(true);
                Object obj = field.get(object);
                reMap.put(fields[i].getName(), obj);
            }
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reMap;
    }

    /**
     * map集合装换为url
     *
     * @param params
     * @return
     */
    public static String hashMapToUrl(Map<String, Object> params) {
        Set<Map.Entry<String, Object>> entrySet = params.entrySet();
        String data = "";
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue() + "";
            if ("rContent".equals(key) || "rTitle".equals(key) || "nickName".equals(key) || "gAddress".equals(key) || "gDesc".equals(key) || "gClassify".equals(key) || "gTitle".equals(key) || "gDeadline".equals(key)) {
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (!"gImgs_list".equals(key)) {
                data += key + "=" + value + "&";
            }
        }
        return data.substring(0, data.length() - 1);
    }
}
