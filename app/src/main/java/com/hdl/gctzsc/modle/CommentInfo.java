package com.hdl.gctzsc.modle;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentInfo implements Parcelable {
    private int cId;// 商品编号
    private String cContent;// 评论内容
    private int uId;// 谁评论的
    private int gId;// 对哪条商品的评论
    private long cTime;// 评论的时间
    private String uNickName;


    protected CommentInfo(Parcel in) {
        cId = in.readInt();
        cContent = in.readString();
        uId = in.readInt();
        gId = in.readInt();
        cTime = in.readLong();
        uNickName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cId);
        dest.writeString(cContent);
        dest.writeInt(uId);
        dest.writeInt(gId);
        dest.writeLong(cTime);
        dest.writeString(uNickName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentInfo> CREATOR = new Creator<CommentInfo>() {
        @Override
        public CommentInfo createFromParcel(Parcel in) {
            return new CommentInfo(in);
        }

        @Override
        public CommentInfo[] newArray(int size) {
            return new CommentInfo[size];
        }
    };

    public String getuNickName() {
        return uNickName;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getgId() {
        return gId;
    }

    public void setgId(int gId) {
        this.gId = gId;
    }

    public long getcTime() {
        return cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
    }


    @Override
    public String toString() {
        return "CommentInfo [cId=" + cId + ", cContent=" + cContent + ", uId="
                + uId + ", gId=" + gId + ", cTime=" + cTime + ", uNickName="
                + uNickName + "]";
    }

    public CommentInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CommentInfo(int cId, String cContent, int uId, int gId, long cTime) {
        super();
        this.cId = cId;
        this.cContent = cContent;
        this.uId = uId;
        this.gId = gId;
        this.cTime = cTime;
    }

}
