package com.wxdmi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 16/3/23.
 */
public class Regex {
    public static final String PHONE = "^0{0,1}(13[0-9]|15[0-9]|18[0-9]|14[5|6|7]|17[0-9])[0-9]{8}$"; // 手机号码

    /**
     * 验证是否是手机号
     *
     * @param phone
     * @return
     */
    public static boolean isMobileNo(String phone) {
        Pattern p = Pattern.compile(PHONE);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
