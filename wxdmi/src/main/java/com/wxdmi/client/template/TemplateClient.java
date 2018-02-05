package com.wxdmi.client.template;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wxdmi.http.HttpRequest;
import com.wxdmi.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 2018/2/5.
 */
public class TemplateClient {

    private static final Logger logger = LoggerFactory.getLogger(TemplateClient.class);

    public TemplateResp templateGet(String token, String url){
        try {
            String requestUrl = url.replace("ACCESS_TOKEN", token);
            String templateString = HttpRequest.sendGet(requestUrl);

            TemplateResp template = GsonUtil.GsonToBean(templateString, TemplateResp.class);
            return template;
        }catch (Exception e){
            logger.error("menu_get_exception/", e.getCause() + ".");
        }
        return null;
    }
}
