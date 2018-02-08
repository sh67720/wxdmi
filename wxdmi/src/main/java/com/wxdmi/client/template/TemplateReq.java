package com.wxdmi.client.template;

/**
 * Created by user on 2018/2/6.
 */
public class TemplateReq {

    private String touser;
    private String template_id;
    private String url;
    private MiniProgram miniprogram;
    private TemplateData data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MiniProgram getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(MiniProgram miniprogram) {
        this.miniprogram = miniprogram;
    }

    public TemplateData getData() {
        return data;
    }

    public void setData(TemplateData data) {
        this.data = data;
    }

    public class MiniProgram{

        private String appid;
        private String pagepath;

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

    public class TemplateData{

        private TemplateDataDetail first;
        private TemplateDataDetail keynote1;
        private TemplateDataDetail keynote2;
        private TemplateDataDetail keynote3;
        private TemplateDataDetail remark;

        public TemplateDataDetail getFirst() {
            return first;
        }

        public void setFirst(TemplateDataDetail first) {
            this.first = first;
        }

        public TemplateDataDetail getKeynote1() {
            return keynote1;
        }

        public void setKeynote1(TemplateDataDetail keynote1) {
            this.keynote1 = keynote1;
        }

        public TemplateDataDetail getKeynote2() {
            return keynote2;
        }

        public void setKeynote2(TemplateDataDetail keynote2) {
            this.keynote2 = keynote2;
        }

        public TemplateDataDetail getKeynote3() {
            return keynote3;
        }

        public void setKeynote3(TemplateDataDetail keynote3) {
            this.keynote3 = keynote3;
        }

        public TemplateDataDetail getRemark() {
            return remark;
        }

        public void setRemark(TemplateDataDetail remark) {
            this.remark = remark;
        }
    }

    public class TemplateDataDetail{

        private String value;
        private String color;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
