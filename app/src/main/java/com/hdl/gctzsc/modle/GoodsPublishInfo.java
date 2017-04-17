package com.hdl.gctzsc.modle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 商品发布信息
 * Created by HDL on 2016/4/17.
 */
public class GoodsPublishInfo {
    private String gTitle;//标题
    private int gClassify;//分类
    private ArrayList<String> gImgs_list;//图片
    private int gPrice;//价格
    private int gOldPrice;//原价
    private String gDesc;//商品描述
    private boolean isPinkage;// 是否包邮
    private boolean isUrgent;//是否紧急出售
    private String gAddress;//地址
    private String gDeadline;//截止时间
    private String gPublishType;// 发布类型

    public String getgPublishType() {
        return gPublishType;
    }

    public void setgPublishType(String gPublishType) {
        this.gPublishType = gPublishType;
    }

    @Override
    public String toString() {
        return "GoodsPublishInfo{" +
                "gTitle='" + gTitle + '\'' +
                ", gClassify='" + gClassify + '\'' +
                ", gImgs_list=" + gImgs_list +
                ", gPrice=" + gPrice +
                ", gOldPrice=" + gOldPrice +
                ", gDesc='" + gDesc + '\'' +
                ", isPinkage=" + isPinkage +
                ", isUrgent=" + isUrgent +
                ", gAddress='" + gAddress + '\'' +
                ", gDeadline='" + gDeadline + '\'' +
                ", gPublishType='" + gPublishType + '\'' +
                '}';
    }

    public String getgTitle() {
        return gTitle;
    }

    public void setgTitle(String gTitle) {
        this.gTitle = gTitle;
    }

    public int getgClassify() {
        return gClassify;
    }

    public void setgClassify(int gClassify) {
        this.gClassify = gClassify;
    }

    public ArrayList<String> getgImgs_list() {
        return gImgs_list;
    }

    public void setgImgs_list(ArrayList<String> gImgs_list) {
        this.gImgs_list = gImgs_list;
    }

    public double getgPrice() {
        return gPrice;
    }

    public void setgPrice(int gPrice) {
        this.gPrice = gPrice;
    }

    public int getgOldPrice() {
        return gOldPrice;
    }

    public void setgOldPrice(int gOldPrice) {
        this.gOldPrice = gOldPrice;
    }

    public String getgDesc() {
        return gDesc;
    }

    public void setgDesc(String gDesc) {
        this.gDesc = gDesc;
    }

    public boolean isPinkage() {
        return isPinkage;
    }

    public void setPinkage(boolean pinkage) {
        isPinkage = pinkage;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public String getgAddress() {
        return gAddress;
    }

    public void setgAddress(String gAddress) {
        this.gAddress = gAddress;
    }

    public String getgDeadline() {
        return gDeadline;
    }

    public void setgDeadline(String gDeadline) {
        this.gDeadline = gDeadline;
    }

    public GoodsPublishInfo(String gTitle, int gClassify, ArrayList<String> gImgs_list,
                            int gPrice, int gOldPrice, String gDesc, boolean isPinkage, boolean isUrgent, String gAddress, String gDeadline) {
        this.gTitle = gTitle;
        this.gClassify = gClassify;
        this.gImgs_list = gImgs_list;
        this.gPrice = gPrice;
        this.gOldPrice = gOldPrice;
        this.gDesc = gDesc;
        this.isPinkage = isPinkage;
        this.isUrgent = isUrgent;
        this.gAddress = gAddress;
        this.gDeadline = gDeadline;
    }

    public GoodsPublishInfo() {
    }
}
