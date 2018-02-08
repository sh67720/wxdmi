package com.wxdmi.http.wxhttp;

import com.wxdmi.entity.BaseEntity;

/**
 * Created by user on 2018/2/6.
 */
public class WXResultBean extends BaseEntity{

    private int errcode;        //返回code，一般0是正常的结果
    private String errmsg;       //返回错误的提示信息

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
