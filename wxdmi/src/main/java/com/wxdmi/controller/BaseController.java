package com.wxdmi.controller;

import com.wxdmi.utils.ParamKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author:chen long
 * @createTime:12-4-19
 */
@Controller
public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * page no
     * @param request
     * @return
     */
    protected int getPageNo(HttpServletRequest request){
        return ServletRequestUtils.getIntParameter(request, ParamKey.Page.NUM_KEY, ParamKey.Page.CURRENT_NO);
    }

    /**
     * page size
     */
    protected int getPageSize(HttpServletRequest request){
        return ServletRequestUtils.getIntParameter(request, ParamKey.Page.SIZE_KEY, ParamKey.Page.PAGE_SIZE);
    }

    /**
     * 重定向时添加消息
     *
     * @param redirectAttrs
     * @param messageText
     */
    protected void addRedirectMessage(RedirectAttributes redirectAttrs, String messageText) {
        redirectAttrs.addFlashAttribute("message", messageText);
    }
    /**
     * 添加错误消息
     *
     * @param messageText
     */
    protected void addModelError(Model model, String messageText) {
        model.addAttribute("error", messageText);
    }
    /**
     * 添加错误消息
     *
     * @param messageText
     */
    protected void addModelSuccess(Model model, String messageText) {
        model.addAttribute("message", messageText);
    }
    /**
     * 重定向时添加错误消息
     *
     * @param redirectAttrs
     * @param messageText
     */
    protected void addRedirectError(RedirectAttributes redirectAttrs, String messageText) {
        redirectAttrs.addFlashAttribute("error", messageText);
    }

    /**
     * 获取登陆页面url
     */
    protected String getLoginUrl() {
        return "redirect:" + OutwardController.LOGIN_PAGE;
    }
    /**
     * 获得重定向url
     *
     * @param redirectUrl
     * @return
     */
    protected String getRedirectUrl(String redirectUrl) {
        String path = "/";
        if (redirectUrl != null) {
            path = UriComponentsBuilder.fromUriString(redirectUrl).build().encode().toUriString();
        }
        return "redirect:" + path;
    }

    /**
     *
     * @param redirectUrl
     * @param defaultUrl
     * @return
     */
    protected String getRedirectUrl(String redirectUrl, String defaultUrl) {
        String path = null;
        if (redirectUrl != null) {
            path = UriComponentsBuilder.fromUriString(redirectUrl).build().encode().toUriString();
        }else{
           path = defaultUrl;
        }
        return "redirect:" + path;
    }

    /**
     * 添加成功消息
     *
     * @param redirectAttrs
     */
    protected void addSaveSuccessMessage(RedirectAttributes redirectAttrs) {
        addRedirectMessage(redirectAttrs, "添加成功");
    }
    /**
     * 更新成功消息
     *
     * @param redirectAttrs
     */
    protected void addUpdateSuccessMessage(RedirectAttributes redirectAttrs) {
        addRedirectMessage(redirectAttrs, "更新成功");
    }
    /**
     * 删除成功消息
     *
     * @param redirectAttrs
     */
    protected void addDeleteSuccessMessage(RedirectAttributes redirectAttrs) {
        addRedirectMessage(redirectAttrs, "删除成功");
    }

    /**
     * 组装后台校验错误信息
     * @param result
     */
    protected String buildErrorMsg(BindingResult result) {
        StringBuffer errorMsg = new StringBuffer();
        List<FieldError> fieldErrors = result.getFieldErrors();

        int size = fieldErrors.size();
        for(int i=0; i<size; i++){
            FieldError error = fieldErrors.get(i);
            errorMsg.append("<li>" + error.getDefaultMessage() + "</li>");
        }

        return errorMsg.toString();
    }
}

