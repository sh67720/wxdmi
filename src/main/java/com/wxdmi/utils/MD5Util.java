package com.wxdmi.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * 
 * MD5加密
 * 
 * @author Cayte
 * @email xusw@dne.com.cn
 * @date 2012-1-1 上午00:00:00
 * 
 */
public class MD5Util {

	public static String getMD5Str(String str) {
		if (str == null || str.equalsIgnoreCase(""))
			return "";
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	
	
	public static String MD5(String str)  
    {  
        MessageDigest md5 = null;  
        try  
        {  
            md5 = MessageDigest.getInstance("MD5"); 
        }catch(Exception e)  
        {  
            e.printStackTrace();  
            return "";  
        }  
          
        char[] charArray = str.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
          
        for(int i = 0; i < charArray.length; i++)  
        {  
            byteArray[i] = (byte)charArray[i];  
        }  
        byte[] md5Bytes = md5.digest(byteArray);  
          
        StringBuffer hexValue = new StringBuffer();  
        for( int i = 0; i < md5Bytes.length; i++)  
        {  
            int val = ((int)md5Bytes[i])&0xff;  
            if(val < 16)  
            {  
                hexValue.append("0");  
            }  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
    } 
	
	
	
	
	/**
	 * 将指定的字符串采用MD5算法加密
	 * 
	 * @param text
	 * @return 加密后的字符串
	 */
	public static String MD5Encode(String text) {
		return MD5Encode(text, "UTF8");
	}

	/**
	 * 将指定的字符串采用MD5算法加密
	 * 
	 * @param text
	 * @return 加密后的字符串
	 */
	public static String MD5EncodeGBK(String text) {
		return MD5Encode(text, "GBK");
	}

	/**
	 * 将指定的字符串采用MD5算法加密,指定的字符串使用指定的字符集编码charset.
	 * 
	 * @param psw
	 *            指定的字符串.
	 * @param charset
	 *            字符集.
	 * @return 返回加密后的字符串.
	 */
	public static String MD5Encode(String psw, String charset) {
		MessageDigest messagedigest = null;
		try {
			messagedigest = MessageDigest.getInstance("MD5"); // 创建消息摘要
			messagedigest.update(psw.getBytes(charset)); // 用明文字符串计算消息摘要。
			byte[] abyte0 = messagedigest.digest(); // 读取消息摘要。

			Base64.Encoder base = Base64.getEncoder();
			return new String(base.encode(abyte0), charset);
		} catch (Exception e) {
			throw new RuntimeException("数据加密出现异常!", e);
		}
	}

	public static void main(String[] args) {
		System.out.println(getMD5Str("001718cfq"));
	}

	/**
	 * 标准MD5加密
	 * @param s
	 * @return
	 */
	public static String MD5Encryption(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes("UTF-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
}
