package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * 个人申明对象
 * 
 * @author 何玲
 * 
 */
public class BmobDeclare extends BmobObject {

	private static final long serialVersionUID = 1L;
	// 申明内容
	private String content = "";
	// 申明版本号
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
