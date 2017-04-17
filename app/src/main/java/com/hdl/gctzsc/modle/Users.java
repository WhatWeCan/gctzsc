package com.hdl.gctzsc.modle;


import cn.bmob.v3.BmobUser;

/**
 * 用户
 */
public class Users extends BmobUser {
    private String uNo;// 用户学号
    private String uPwd;// 用户密码
    private boolean isMan;// 用户性别

    public String getuNo() {
        return uNo;
    }

    public void setuNo(String uNo) {
        this.uNo = uNo;
    }

    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String uPwd) {
        this.uPwd = uPwd;
    }

    public boolean isMan() {
        return isMan;
    }

    public void setMan(boolean man) {
        isMan = man;
    }
}
