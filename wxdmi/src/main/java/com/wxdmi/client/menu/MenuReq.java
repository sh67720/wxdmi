package com.wxdmi.client.menu;

import javafx.scene.control.MenuButton;

import java.util.List;

/**
 * 请求创建菜单实体类
 * Created by ShenHua on 2018/2/5.
 */
public class MenuReq {

    private List<MenuButtonReq> button;

    public List<MenuButtonReq> getButton() {
        return button;
    }

    public void setButton(List<MenuButtonReq> button) {
        this.button = button;
    }

    public class MenuButtonReq{

        private List<MenuButtonReq> sub_button;
        private String type;
        private String name;
        private String key;
        private String url;
        private String media_id;
        private String appid;
        private String pagepath;

        public List<MenuButtonReq> getSub_button() {
            return sub_button;
        }

        public void setSub_button(List<MenuButtonReq> sub_button) {
            this.sub_button = sub_button;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagepath() {
            return pagepath;
        }

        public void setPagepath(String pagepath) {
            this.pagepath = pagepath;
        }
    }
}
