package com.wxdmi.http.wxhttp;

/**
 * Created by user on 2018/2/6.
 */
public class WXResultHandler {

    public void handler(WXResultBean wxResultBean){
        int errcode = wxResultBean.getErrcode();
        String errmsg = wxResultBean.getErrmsg();

    }
}
