package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobObject;

/**
 * ϵͳ״̬����
 * 
 * @author ����
 * 
 */
public class BmobSystemStatus extends BmobObject {

	private static final long serialVersionUID = 1L;

	private Integer status = 0;// ϵͳ״̬,<0��ʾ�쳣��

	private String info = "";// ϵͳ״̬��Ϣ

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
