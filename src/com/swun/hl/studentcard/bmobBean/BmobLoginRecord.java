package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * ��¼��¼����¼��¼��ַ���豸
 * 
 * @author ����
 * 
 */
public class BmobLoginRecord extends BmobObject {

	private static final long serialVersionUID = 1L;
	// ��¼���˻�
	private BmobAccount account;
	// ��¼ʱ��
	private String loginTime;
	// �ֻ�����
	private String phoneFactory = "";
	// �ֻ��ͺ�
	private String phoneType = "";
	// �ֻ���Ψһʶ���
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
