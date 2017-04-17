package com.hdl.gctzsc.modle;

public class UrgentGoods {
	private int uId;// 谁的商品
	private int gId;// 哪个商品
	private String gTitle;// 标题
	private int gClassify;// 分类

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

	public UrgentGoods(int uId, int gId, String gTitle, int gClassify) {
		super();
		this.uId = uId;
		this.gId = gId;
		this.gTitle = gTitle;
		this.gClassify = gClassify;
	}

	public UrgentGoods() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "UrgentGoods [uId=" + uId + ", gId=" + gId + ", gTitle="
				+ gTitle + ", gClassify=" + gClassify + "]";
	}

}
