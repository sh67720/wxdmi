package com.wxdmi.client.menu;

import com.alibaba.fastjson.JSONObject;
import com.wxdmi.client.common.AccessTokenClient;
import com.wxdmi.http.HttpRequest;
import com.wxdmi.utils.GsonUtil;
import com.wxdmi.utils.cache.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2018/2/5.
 */
@Service
public class MenuClient {

    private static final Logger logger = LoggerFactory.getLogger(MenuClient.class);
    @Autowired
    private AccessTokenClient accessTokenClient;
    @Value("${wx.menu.create}")
    private String menuCreate;
    @Value("${wx.menu.get}")
    private String menuGet;
    @Value("${wx.menu.delete}")
    private String menuDelete;

    //创建菜单
    public int menuCreate(MenuReq req){
        try {
            String requestUrl = menuCreate.replace("ACCESS_TOKEN", accessTokenClient.getAccessToken());

            JSONObject jsonObject = JSONObject.parseObject(HttpRequest.sendPost(requestUrl, GsonUtil.GsonString(req)));
            int errcode = jsonObject.getInteger("errcode");
            String errmsg = jsonObject.getString("errmsg");
            return errcode;
        }catch (Exception e){
            logger.error("menu_create_exception/", e.getCause() + ".");
        }
        return -1;
    }

    //查询菜单
    public String menuGet(){
        try {
            String requestUrl = menuGet.replace("ACCESS_TOKEN", accessTokenClient.getAccessToken());

            JSONObject jsonObject = JSONObject.parseObject(HttpRequest.sendGet(requestUrl));
            int errcode = jsonObject.getInteger("errcode");
            String errmsg = jsonObject.getString("errmsg");
        }catch (Exception e){
            logger.error("menu_get_exception/", e.getCause() + ".");
        }
        return "";
    }

    //删除菜单
    public int menuDelete(){
        try {
            String requestUrl = menuDelete.replace("ACCESS_TOKEN", accessTokenClient.getAccessToken());

            JSONObject jsonObject = JSONObject.parseObject(HttpRequest.sendGet(requestUrl));
            int errcode = jsonObject.getInteger("errcode");
            String errmsg = jsonObject.getString("errmsg");
            return errcode;
        }catch (Exception e){
            logger.error("menu_delete_exception/", e.getCause() + ".");
        }
        return -1;
    }
}
