package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * Bmob反馈对象
 * 
 * @author LANTINGSHUXU
 * 
 */
public class BmobFeedBack extends BmobObject {

	private static final long serialVersionUID = 1L;

	// 反馈内容
	private String content = "";

	// 联系方式
	private String contact = "";

	// 应用版本号
	private String appVersionName = "";

	// 安卓系统版本
	private String androidVersion = "";

	// 手机厂商
	private String phoneFactory = "";

	// 手机型号
	private String phoneType = "";

	// 手机的IMEI
	private String phoneIMEI = "";

	// 手机的唯一识别号
	private String phoneFingerPrint = "";

	// 反馈的账户
	private BmobAccount account;

	public BmobAccount getAccount() {
		return account;
	}

	public void setAccount(BmobAccount account) {
		this.account = account;
	}

	public String getPhoneFingerPrint() {
		return phoneFingerPrint;
	}

	public void setPhoneFingerPrint(String phoneFingerPrint) {
		this.phoneFingerPrint = phoneFingerPrint;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
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

	public String getPhoneIMEI() {
		return phoneIMEI;
	}

	public void setPhoneIMEI(String phoneIMEI) {
		this.phoneIMEI = phoneIMEI;
	}

}
