package com.wxdmi.controller;

import com.wxdmi.client.common.AccessTokenClient;
import com.wxdmi.client.menu.MenuClient;
import com.wxdmi.client.menu.MenuReq;
import com.wxdmi.entity.Admin;
import com.wxdmi.service.AdminService;
import com.wxdmi.utils.MD5Util;
import com.wxdmi.utils.cache.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/outward")
public class OutwardController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(OutwardController.class);

    public final static String ERROR_PAGE = "/outward/error";
    public final static String LOGIN_PAGE = "/outward/login";
    public final static String ADMIN_HOME_PAGE = "/outward/adminHome";

    @Autowired
    private AdminService adminService;
    @Autowired
    private AccessTokenClient accessTokenClient;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String login(Model model){
        String st = accessTokenClient.getAccessToken();
        return "获取到的token为："+st;
    }

    @RequestMapping(value = "/adminHome", method = RequestMethod.GET)
    public String home(Model model){
        return ADMIN_HOME_PAGE;
    }

    /**
     * 处理登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, Admin admin, HttpServletRequest request) throws Exception {
        String username = admin.getUsername();
        String password = admin.getPassword();
        admin.setPassword(null);
        model.addAttribute("admin", admin);
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            addModelError(model, "用户名或密码不能为空!");
            return LOGIN_PAGE;
        }
        Admin dbAdmin = adminService.login(username, MD5Util.getMD5Str(password));
        if (dbAdmin == null) {
            addModelError(model, "用户名或密码错误!");
            return LOGIN_PAGE;
        }
        HttpSession session = request.getSession();
        session.setAttribute("admin", dbAdmin);
        return getRedirectUrl(ADMIN_HOME_PAGE);
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(HttpServletRequest request, Model model) {
        return ERROR_PAGE;
    }


    /********************* begin private method ****************/

}
