package com.zyy.pic.entity.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/16.
 */
public class VoSimplePic implements Serializable {
    private String type;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
