package com.wxdmi.controller;

import com.wxdmi.entity.Admin;
import com.wxdmi.utils.WXCheckUtil;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 公众号验证接口
 * Created by user on 2018/1/17.
 */
@Controller
public class WXValidateController {

    private static final Logger logger = LoggerFactory.getLogger(WXValidateController.class);

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ResponseBody
    public String validate(Model model, HttpServletRequest request){
        //验证服务器地址有效性：微信服务器将发送GET请求到填写的服务器地址URL上，GET请求携带四个参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        if(!TextUtils.isEmpty(signature)&&
                !TextUtils.isEmpty(timestamp)&&
                !TextUtils.isEmpty(nonce)&&
                !TextUtils.isEmpty(echostr)&&
                WXCheckUtil.checkSignature(signature, timestamp, nonce)){
            logger.info("signature:" + "验证成功");
            return echostr;
        }
        logger.error("signature:" + "验证失败");
        return "验证失败";
    }
}
