package com.wxdmi.client.template;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
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
public class TemplateClient {

    private static final Logger logger = LoggerFactory.getLogger(TemplateClient.class);
    @Autowired
    private AccessTokenClient accessTokenClient;
    @Value("${wx.template.getall}")
    private String templateGetAll;
    @Value("${wx.template.send}")
    private String templateSend;

    //查询所有消息模板的列表
    public TemplateResp templateGetAll(){
        try {
            String requestUrl = templateGetAll.replace("ACCESS_TOKEN", accessTokenClient.getAccessToken());
            String templateString = HttpRequest.sendGet(requestUrl);

            TemplateResp template = GsonUtil.GsonToBean(templateString, TemplateResp.class);
            return template;
        }catch (Exception e){
            logger.error("template_get_exception/", e.getCause() + ".");
        }
        return null;
    }

    //发送消息模板
    public int templateSend(TemplateReq templateReq){
        try {
            String requestUrl = templateSend.replace("ACCESS_TOKEN", accessTokenClient.getAccessToken());

            JSONObject jsonObject = JSONObject.parseObject(HttpRequest.sendPost(requestUrl, GsonUtil.GsonString(templateReq)));
            int errcode = jsonObject.getInteger("errcode");
            String errmsg = jsonObject.getString("errmsg");
            return errcode;
        }catch (Exception e){
            logger.error("template_send_exception/", e.getCause() + ".");
        }
        return -1;
    }
}
