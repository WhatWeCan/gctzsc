package com.hdl.gctzsc.modle;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 商品类  最新发布
 */
public class Goods extends BmobObject {
    private String gTitle;// 标题
    private Integer uId;// 发布人
    private String uNickName;// 发布人的昵称
    private Integer gClassify;// 分类
    private List<String> gImgUrls;// 商品图片地址
    private Integer gPrice;// 售价
    private Integer gOldPrice;// 原价
    private Byte gPublishType;// 发布类型
    private Boolean gIsSaled;// 是否卖出---对应g_state
    private String gDesc;// 商品信息/简介
    private Boolean isPinkage;// 是否包邮
    private Boolean isUrgent;// 是否紧急出售
    private String gAddress;// 发布人地址
    private Byte gBrowCount;// 浏览量
    private Byte gNice;// 点赞数量
    private Integer gCommentCount;// 评论条数
    private int gMaxPrice;
    private long gDeaLine;

    public String getgTitle() {
        return gTitle;
    }

    public void setgTitle(String gTitle) {
        this.gTitle = gTitle;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getuNickName() {
        return uNickName;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public Integer getgClassify() {
        return gClassify;
    }

    public void setgClassify(Integer gClassify) {
        this.gClassify = gClassify;
    }

    public List<String> getgImgUrls() {
        return gImgUrls;
    }

    public void setgImgUrls(List<String> gImgUrls) {
        this.gImgUrls = gImgUrls;
    }

    public Integer getgPrice() {
        return gPrice;
    }

    public void setgPrice(Integer gPrice) {
        this.gPrice = gPrice;
    }

    public Integer getgOldPrice() {
        return gOldPrice;
    }

    public void setgOldPrice(Integer gOldPrice) {
        this.gOldPrice = gOldPrice;
    }

    public Byte getgPublishType() {
        return gPublishType;
    }

    public void setgPublishType(Byte gPublishType) {
        this.gPublishType = gPublishType;
    }

    public Boolean getgIsSaled() {
        return gIsSaled;
    }

    public void setgIsSaled(Boolean gIsSaled) {
        this.gIsSaled = gIsSaled;
    }

    public String getgDesc() {
        return gDesc;
    }

    public void setgDesc(String gDesc) {
        this.gDesc = gDesc;
    }

    public Boolean getPinkage() {
        return isPinkage;
    }

    public void setPinkage(Boolean pinkage) {
        isPinkage = pinkage;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }

    public String getgAddress() {
        return gAddress;
    }

    public void setgAddress(String gAddress) {
        this.gAddress = gAddress;
    }

    public Byte getgBrowCount() {
        return gBrowCount;
    }

    public void setgBrowCount(Byte gBrowCount) {
        this.gBrowCount = gBrowCount;
    }

    public Byte getgNice() {
        return gNice;
    }

    public void setgNice(Byte gNice) {
        this.gNice = gNice;
    }

    public Integer getgCommentCount() {
        return gCommentCount;
    }

    public void setgCommentCount(Integer gCommentCount) {
        this.gCommentCount = gCommentCount;
    }

    public int getgMaxPrice() {
        return gMaxPrice;
    }

    public void setgMaxPrice(int gMaxPrice) {
        this.gMaxPrice = gMaxPrice;
    }

    public long getgDeaLine() {
        return gDeaLine;
    }

    public void setgDeaLine(long gDeaLine) {
        this.gDeaLine = gDeaLine;
    }
}
