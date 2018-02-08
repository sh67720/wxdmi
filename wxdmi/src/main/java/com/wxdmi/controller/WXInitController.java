package com.wxdmi.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import com.wxdmi.client.common.AccessTokenClient;
import com.wxdmi.client.menu.MenuClient;
import com.wxdmi.client.menu.MenuReq;
import com.wxdmi.client.template.TemplateClient;
import com.wxdmi.client.template.TemplateReq;
import com.wxdmi.client.template.TemplateResp;
import com.wxdmi.constant.CacheConstant;
import com.wxdmi.entity.Admin;
import com.wxdmi.enums.EventType;
import com.wxdmi.enums.MessageType;
import com.wxdmi.utils.*;
import com.wxdmi.utils.cache.CacheUtil;
import org.apache.commons.lang3.Range;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 公众号验证接口
 * Created by user on 2018/1/17.
 */
@Controller
public class WXInitController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(WXInitController.class);
    @Autowired
    private MenuClient menuClient;
    @Autowired
    private TemplateClient templateClient;

    //签名验证
    @RequestMapping(value = "/wxInterface", method = RequestMethod.GET)
    @ResponseBody
    public String wxInterface(Model model, HttpServletRequest request){
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
        logger.info("signature:" + "验证失败");
        return "验证失败";
    }

    //处理消息接收
    @RequestMapping(value = "/wxInterface", method = RequestMethod.POST)
    @ResponseBody
    public String wxInterface(Model model, Admin admin, HttpServletRequest request){
        // TODO 接收、处理、响应由微信服务器转发的用户发送给公众帐号的消息
        String responseMessage = "";
        Map<String,String> map = null;
        try {
            //将请求、响应的编码均设置为UTF-8（防止中文乱码）
            request.setCharacterEncoding("UTF-8");
            //解析微信发来的请求,将解析后的结果封装成Map返回
            map = ParseXmlUtils.saxParseXml(request);
        } catch (Exception e) {
            logger.error("responseMessage/" + e.getMessage());
        }
        logger.info("requestMessage/" + Utils.mapToString(map) + ".");
        responseMessage = buildResponseMessage(map);
        if(responseMessage.equals(""))logger.info("responseMessage/" + responseMessage + ".");
        else logger.info("responseMessage/" + responseMessage + ".");
        return responseMessage;
    }

    //初始化信息
    @RequestMapping(value = "/wxInit", method = RequestMethod.GET)
    @ResponseBody
    public String init(Model model){
        StringBuffer sb = new StringBuffer();

        MenuReq menuReq = new MenuReq();
        List<MenuReq.MenuButtonReq> menuButtonReqList = new ArrayList<>();

        MenuReq.MenuButtonReq menuButtonReq1 = menuReq.new MenuButtonReq();
        menuButtonReq1.setName("测试1");
        menuButtonReq1.setType("click");
        menuButtonReq1.setKey("menu1");
        menuButtonReqList.add(menuButtonReq1);

        MenuReq.MenuButtonReq menuButtonReq2 = menuReq.new MenuButtonReq();
        menuButtonReq2.setName("测试2");
        menuButtonReq2.setType("view");
        menuButtonReq2.setUrl("http://www.shdmi.cn");
        menuButtonReqList.add(menuButtonReq2);

        MenuReq.MenuButtonReq menuButtonReq3 = menuReq.new MenuButtonReq();
        menuButtonReq3.setName("测试3");
        List<MenuReq.MenuButtonReq> menuSButtonReqList = new ArrayList<>();

        MenuReq menuReqSub = new MenuReq();
        MenuReq.MenuButtonReq menuSButtonReq1 = menuReqSub.new MenuButtonReq();
        menuSButtonReq1.setName("测试4");
        menuSButtonReq1.setType("pic_sysphoto");
        menuSButtonReq1.setKey("rselfmenu_1_0");
        menuSButtonReqList.add(menuSButtonReq1);

        MenuReq.MenuButtonReq menuSButtonReq2 = menuReqSub.new MenuButtonReq();
        menuSButtonReq2.setName("测试5");
        menuSButtonReq2.setType("pic_weixin");
        menuSButtonReq2.setKey("rselfmenu_1_2");
        menuSButtonReqList.add(menuSButtonReq2);

        MenuReq.MenuButtonReq menuSButtonReq3 = menuReqSub.new MenuButtonReq();
        menuSButtonReq3.setName("测试6");
        menuSButtonReq3.setType("click");
        menuSButtonReq3.setKey("menu6");
        menuSButtonReqList.add(menuSButtonReq3);

        menuButtonReq3.setSub_button(menuSButtonReqList);
        menuButtonReqList.add(menuButtonReq3);

        menuReq.setButton(menuButtonReqList);

        if(menuClient.menuDelete() == 0) {
            menuClient.menuCreate(menuReq);
            sb.append("Menu初始化完成 ");
        }else if(menuClient.menuDelete() == 40001){
            getRedirectUrl("/wxInit");
        }
        return sb.toString();
    }

    //发送模板信息
    @RequestMapping(value = "/send/{user}", method = RequestMethod.GET)
    @ResponseBody
    public String sendTemplate(Model model, @PathVariable String user){
        TemplateResp templateResp = templateClient.templateGetAll();
        if(templateResp != null) {
            List<TemplateResp.TemplateContent> template_list = templateResp.getTemplate_list();
            if(template_list != null&&template_list.size() > 0){
                TemplateResp.TemplateContent templateContent = template_list.get(template_list.size() - 1);

                TemplateReq templateReq = new TemplateReq();
                templateReq.setTouser(user);
                templateReq.setTemplate_id(templateContent.getTemplate_id());
                templateReq.setUrl("http://www.shdmi.cn");

                TemplateReq.TemplateData templateData = templateReq.new TemplateData();

                TemplateReq.TemplateDataDetail templateDataDetail = templateReq.new TemplateDataDetail();
                templateDataDetail.setValue("恭喜你购买成功！");
                templateDataDetail.setColor("#173177");
                templateData.setFirst(templateDataDetail);

                TemplateReq.TemplateDataDetail templateDataDetai1 = templateReq.new TemplateDataDetail();
                templateDataDetai1.setValue("巧克力");
                templateDataDetai1.setColor("#FF0000");
                templateData.setKeynote1(templateDataDetai1);

                TemplateReq.TemplateDataDetail templateDataDetai2 = templateReq.new TemplateDataDetail();
                templateDataDetai2.setValue("39.8元");
                templateDataDetai2.setColor("#FF0000");
                templateData.setKeynote2(templateDataDetai2);

                TemplateReq.TemplateDataDetail templateDataDetai3 = templateReq.new TemplateDataDetail();
                templateDataDetai3.setValue(DateUtil.dateToStr(new Date(), "yyyy年MM月dd日"));
                templateDataDetai3.setColor("#FF0000");
                templateData.setKeynote3(templateDataDetai3);

                TemplateReq.TemplateDataDetail templateDataDetai4 = templateReq.new TemplateDataDetail();
                templateDataDetai4.setValue("欢迎再次购买！");
                templateDataDetai4.setColor("#173177");
                templateData.setRemark(templateDataDetai4);

                templateReq.setData(templateData);
                templateClient.templateSend(templateReq);
            }
        }
        return "模板发送成功";
    }

    /**
     * https://www.cnblogs.com/xdp-gacl/p/5161206.html
     * 根据消息类型构造返回消息
     * @param map 封装了解析结果的Map
     * @return responseMessage(响应消息)
     */
    public String buildResponseMessage(Map map) {
        //响应消息
        String responseMessage = "";
        //得到消息类型
        String msgType = map.get("MsgType").toString();
        //消息类型
        MessageType messageEnumType = MessageType.valueOf(MessageType.class, msgType.toUpperCase());
        switch (messageEnumType) {
            case TEXT:
                //处理文本消息
                //responseMessage = handleTextMessage(map);
                responseMessage = BuildXmlUtils.buildTextMessage(map, "你是猪哦！");
                break;
            case IMAGE:
                //处理图片消息
                //responseMessage = handleImageMessage(map);
                break;
            case VOICE:
                //处理语音消息
                //responseMessage = handleVoiceMessage(map);
                break;
            case VIDEO:
                //处理视频消息
                //responseMessage = handleVideoMessage(map);
                break;
            case SHORTVIDEO:
                //处理小视频消息
                //responseMessage = handleSmallVideoMessage(map);
                break;
            case LOCATION:
                //处理位置消息
                //responseMessage = handleLocationMessage(map);
                break;
            case LINK:
                //处理链接消息
                //responseMessage = handleLinkMessage(map);
                break;
            case EVENT:
                //处理事件消息,用户在关注与取消关注公众号时，微信会向我们的公众号服务器发送事件消息,开发者接收到事件消息后就可以给用户下发欢迎消息
                //responseMessage = handleEventMessage(map);
                String event = map.get("Event").toString();
                EventType eventType = EventType.valueOf(EventType.class, event.toUpperCase());
                switch (eventType) {
                    case SUBSCRIBE:
                        //订阅
                        //发送回复订阅模板
                        TemplateResp templateResp = templateClient.templateGetAll();
                        if(templateResp != null) {
                            List<TemplateResp.TemplateContent> template_list = templateResp.getTemplate_list();
                            if(template_list != null&&template_list.size() > 0){
                                TemplateResp.TemplateContent templateContent = template_list.get(0);

                                TemplateReq templateReq = new TemplateReq();
                                templateReq.setTouser(map.get("FromUserName").toString());
                                templateReq.setTemplate_id(templateContent.getTemplate_id());
                                templateReq.setUrl("http://www.shdmi.cn");
                                templateClient.templateSend(templateReq);
                            }
                        }

                        //获取用户信息并保存

                        break;
                    case UNSUBSCRIBE:
                        //取消订阅
                        break;
                    default:
                        responseMessage = BuildXmlUtils.buildTextMessage(map, "你是猪吗！");
                        break;
                }
            default:
                break;
        }
        //返回响应消息
        return responseMessage;
    }
}
