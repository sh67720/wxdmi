package com.wxdmi.client.menu;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wxdmi.constant.CacheConstant;
import com.wxdmi.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 2018/2/5.
 */
public class MenuClient {

    private static final Logger logger = LoggerFactory.getLogger(MenuClient.class);

    //创建菜单
    public int menuCreate(String token, String url, MenuReq req){
        try {
            String requestUrl = url.replace("ACCESS_TOKEN", token);

            Gson gson = new Gson();
            String param = gson.toJson(req);
            logger.info("menu_create_req/" + param + ".");
            JSONObject jsonObject = JSONObject.parseObject(HttpRequest.sendPost(requestUrl, param));
            int errcode = jsonObject.getInteger("errcode");
            String errmsg = jsonObject.getString("errmsg");
            if(errcode == 0){
                logger.info("menu_create_success/"+errcode+ "/" + errmsg + ".");
            }else{
                logger.info("menu_create_fail/"+errcode+ "/" + errmsg + ".");
            }
            return errcode;
        }catch (Exception e){
            logger.error("menu_create_exception/", e.getCause() + ".");
        }
        return -1;
    }

    //查询菜单
    public String menuGet(String token, String url){
        try {
            String requestUrl = url.replace("ACCESS_TOKEN", token);

            JSONObject jsonObject = JSONObject.parseObject(HttpRequest.sendGet(requestUrl));
            int errcode = jsonObject.getInteger("errcode");
            String errmsg = jsonObject.getString("errmsg");
            if(errcode == 0) logger.info("menu_get_success/"+errcode+ "/" + errmsg + ".");
            else logger.info("menu_get_fail/"+errcode+ "/" + errmsg + ".");
        }catch (Exception e){
            logger.error("menu_get_exception/", e.getCause() + ".");
        }
        return "";
    }

    //删除菜单
    public int menuDelete(String token, String url){
        try {
            String requestUrl = url.replace("ACCESS_TOKEN", token);

            JSONObject jsonObject = JSONObject.parseObject(HttpRequest.sendGet(requestUrl));
            int errcode = jsonObject.getInteger("errcode");
            String errmsg = jsonObject.getString("errmsg");
            if(errcode == 0) {
                logger.info("menu_delete_success/" + errcode + "/" + errmsg + ".");
            }else{
                logger.info("menu_delete_fail/"+errcode+ "/" + errmsg + ".");
            }
            return errcode;
        }catch (Exception e){
            logger.error("menu_delete_exception/", e.getCause() + ".");
        }
        return -1;
    }
}
