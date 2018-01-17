package com.wxdmi.utils;

/**
 * Created by admin on 2015/6/2.
 */
public interface ParamKey {
    interface Page {
        String NUM_KEY = "pageNo";
        String SIZE_KEY = "pageSize";
        String OBJECT = "pageObj";
        String SKIP_URL = "pageSkipUrl";
        String DESC = "desc";
        String ASC = "asc";

        int CURRENT_NO = 1;
        int PAGE_SIZE = 10;
    }

}
