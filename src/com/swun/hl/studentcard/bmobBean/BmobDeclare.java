package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * ������������
 * 
 * @author ����
 * 
 */
public class BmobDeclare extends BmobObject {

	private static final long serialVersionUID = 1L;
	// ��������
	private String content = "";
	// �����汾��
	private Integer versionCode = 0;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

}
