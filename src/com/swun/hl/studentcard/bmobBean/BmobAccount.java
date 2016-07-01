package com.swun.hl.studentcard.bmobBean;

import cn.bmob.v3.BmobUser;

/**
 * Bmob账户javaBean对象
 * 
 * @author LANTINGSHUXU
 * 
 */
public class BmobAccount extends BmobUser {
	private static final long serialVersionUID = 1L;
	private Boolean sex = true;// true表示男。false表示女
	private String nick = "";
	private Integer status = 0;// 用户状态（默认为0）。< 0 表示账户冻结；>=0为正常

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

}
