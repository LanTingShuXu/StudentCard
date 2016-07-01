package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * 登录记录表，记录登录地址和设备
 * 
 * @author 何玲
 * 
 */
public class BmobLoginRecord extends BmobObject {

	private static final long serialVersionUID = 1L;
	// 登录的账户
	private BmobAccount account;
	// 登录时间
	private String loginTime;
	// 手机厂商
	private String phoneFactory = "";
	// 手机型号
	private String phoneType = "";
	// 手机的唯一识别号
	private String phoneFingerPrint = "";

	public BmobAccount getAccount() {
		return account;
	}

	public void setAccount(BmobAccount account) {
		this.account = account;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getPhoneFactory() {
		return phoneFactory;
	}

	public void setPhoneFactory(String phoneFactory) {
		this.phoneFactory = phoneFactory;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneFingerPrint() {
		return phoneFingerPrint;
	}

	public void setPhoneFingerPrint(String phoneFingerPrint) {
		this.phoneFingerPrint = phoneFingerPrint;
	}

}
