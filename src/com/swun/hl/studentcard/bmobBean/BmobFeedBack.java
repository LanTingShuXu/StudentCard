package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * Bmob��������
 * 
 * @author LANTINGSHUXU
 * 
 */
public class BmobFeedBack extends BmobObject {

	private static final long serialVersionUID = 1L;

	// ��������
	private String content = "";

	// ��ϵ��ʽ
	private String contact = "";

	// Ӧ�ð汾��
	private String appVersionName = "";

	// ��׿ϵͳ�汾
	private String androidVersion = "";

	// �ֻ�����
	private String phoneFactory = "";

	// �ֻ��ͺ�
	private String phoneType = "";

	// �ֻ���IMEI
	private String phoneIMEI = "";

	// �ֻ���Ψһʶ���
	private String phoneFingerPrint = "";

	// �������˻�
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
