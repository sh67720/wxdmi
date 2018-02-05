package com.wxdmi.client.common;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wxdmi.constant.CacheConstant;
import com.wxdmi.constant.Constants;
import com.wxdmi.entity.client.AccessToken;
import com.wxdmi.http.HttpRequest;
import com.wxdmi.utils.cache.CacheUtil;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2018/1/18.
 */
public class AccessTokenClient {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenClient.class);

    /**
     * accessToken:(ACCESS_TOKEN长期有效).
     * @param  @return     设定文件
     * @throws String   DOM对象
     */
    public String getAccessToken(String tokenUrl, CacheUtil cacheUtil) {
        String token = "";
        int seconds = 7200;
        try {
            AccessToken accessToken = cacheUtil.getCacheByFromRedis(CacheConstant.ACCESSTOKEN_KEY, AccessToken.class);
            if(accessToken != null)token = accessToken.getAccessToken();
            if (TextUtils.isEmpty(token)) {
                AccessToken accessTokenNew = accessToken(tokenUrl);
                token = accessTokenNew.getAccessToken();
                seconds = accessTokenNew.getExpiresIn();
                cacheUtil.putData2RedisByTime(CacheConstant.ACCESSTOKEN_KEY, seconds, accessTokenNew);
                logger.info("/接口获取token=" + token+ "/");
            }else {
                logger.info("/redis获取token=" + token + "/");
            }
        } catch (Exception e) {
            if (TextUtils.isEmpty(token)) {
                token = accessToken(tokenUrl).getAccessToken();//腾讯获取 不是长期有效的
                logger.error("/redis服务器未开启,请运维人员去服务器开启redis服务器!/",e);
            }
        }
        return token;
    }

    /**
     * 获取access_token
     * // 获取access_token的接口地址（GET） 限2000（次/天）
     * @return

     */
    public AccessToken accessToken(String tokenUrl) {
        AccessToken accessToken = null;
        String requestUrl = tokenUrl.replace("APPID", Constants.WX_APPID).replace("APPSECRET", Constants.WX_APPSECRET);
        //JSONObject jsonObject = new JSONObject(HttpRequest.sendGet(requestUrl));
        JSONObject jsonObject = JSONObject.parseObject(HttpRequest.sendGet(requestUrl));
        if (null != jsonObject) {// 如果请求成功
            try {
                accessToken = new AccessToken();
                accessToken.setAccessToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
            } catch (JSONException e) {
                accessToken = null;// 获取token失败
                logger.error(jsonObject.getString("获取token失败 errcode:{"+ jsonObject.getInteger("errcode")+"} errmsg:{"+jsonObject.getString("errmsg"))+"}");
            }
        }
        return accessToken;
    }
}
