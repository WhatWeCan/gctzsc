package com.hdl.gctzsc.modle;

/**
 * 求购商品
 * Created by HDL on 2016/4/17.
 */
public class RequestGoodsInfo {
    private String rTitle;
    private String rContent;

    @Override
    public String toString() {
        return "RequestGoodsInfo{" +
                "rTitle='" + rTitle + '\'' +
                ", rContent='" + rContent + '\'' +
                '}';
    }

    public String getrTitle() {
        return rTitle;
    }

    public void setrTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public String getrContent() {
        return rContent;
    }

    public void setrContent(String rContent) {
        this.rContent = rContent;
    }

    public RequestGoodsInfo(String rTitle, String rContent) {
        this.rTitle = rTitle;
        this.rContent = rContent;
    }

    public RequestGoodsInfo() {
    }
}
