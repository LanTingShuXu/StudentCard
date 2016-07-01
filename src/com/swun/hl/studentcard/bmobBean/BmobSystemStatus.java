package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * 系统状态对象
 * 
 * @author 何玲
 * 
 */
public class BmobSystemStatus extends BmobObject {

	private static final long serialVersionUID = 1L;

	private Integer status = 0;// 系统状态,<0表示异常。

	private String info = "";// 系统状态信息

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
